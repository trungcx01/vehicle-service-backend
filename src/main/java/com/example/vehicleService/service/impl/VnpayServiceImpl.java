package com.example.vehicleService.service.impl;

import com.example.vehicleService.config.VnpayConfig;
import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.dto.VnpayResponseDTO;
import com.example.vehicleService.entity.Appointment;

import com.example.vehicleService.entity.BaseService;
import com.example.vehicleService.entity.Payment;
import com.example.vehicleService.entity.Proposal;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.mapper.PaymentMapper;
import com.example.vehicleService.repository.PaymentRepository;
import com.example.vehicleService.repository.ProposalRepository;
import com.example.vehicleService.service.VnpayService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VnpayServiceImpl implements VnpayService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ProposalRepository proposalRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public VnpayServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, ProposalRepository proposalRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.proposalRepository = proposalRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public VnpayResponseDTO createPayment(HttpServletRequest request, Double amount, String orderNote) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;
        String vnp_Locale = "vn";
        String vnp_CurrCode = "VND";
        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        String vnp_OrderInfo = orderNote;
        String vnp_OrderType = "other";
        String vnp_Amount = String.valueOf((long)(amount * 100));
        String vnp_ReturnUrl = VnpayConfig.vnp_ReturnUrl;
        String vnp_IpAddr = VnpayConfig.getIpAddress(request);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Amount", vnp_Amount);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        vnp_Params.put("vnp_BankCode", "NCB");
        //Build data to hash and querystring
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build query
                try {
                    query.append(URLEncoder.encode(fieldName,
                            StandardCharsets.US_ASCII.toString()));
                    hashData.append(URLEncoder.encode(fieldName,
                            StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                query.append('=');
                hashData.append('=');
                try {
                    query.append(URLEncoder.encode(fieldValue,
                            StandardCharsets.US_ASCII.toString()));
                    hashData.append(URLEncoder.encode(fieldValue,
                            StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        System.out.println(hashData);
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey,
                hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return new VnpayResponseDTO((VnpayConfig.vnp_PayUrl + "?" + queryUrl), vnp_TxnRef);
    }

    @Override
    public PaymentDTO handleVnpayPaymentReturn(HttpServletRequest request,   HttpServletResponse response) throws IOException {
        String transactionReference = request.getParameter("vnp_TxnRef");
        String responseCode = request.getParameter("vnp_ResponseCode");
        String orderInfo = request.getParameter("vnp_OrderInfo");
        Payment payment = paymentRepository.findByTransactionReference(transactionReference);
        if (responseCode.equals("00")){
            payment.setStatus(Status.FINISHED);
            payment.setOrderInfo(orderInfo);
            BaseService baseService = payment.getBaseService();
            if (baseService instanceof Proposal ){
                Proposal proposal = proposalRepository.findById(baseService.getId()).orElseThrow(
                        () -> new EntityNotFoundException("Not found Proposal!")
                );
                simpMessagingTemplate.convertAndSendToUser(proposal.getShop().getUser().getUsername(), "/queue/proposal",
                        "ACCEPTED_PROPOSAL: " + proposal.getId() + " FOR EMERGENCY_REQUEST: " + proposal.getEmergencyRequest().getId());
            }
            response.sendRedirect("http://localhost:4200/payment-success/" + payment.getId());
        }
        else{
            payment.setStatus(Status.CANCELED);
            payment.setOrderInfo(orderInfo);
            response.sendRedirect("http://localhost:4200/payment-success/" + payment.getId());
        }

        return paymentMapper.toDto(paymentRepository.save(payment));
    }
}
