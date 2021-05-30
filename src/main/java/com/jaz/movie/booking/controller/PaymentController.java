package com.jaz.movie.booking.controller;

import com.jaz.movie.booking.config.PaytmConfig;
import com.jaz.movie.booking.dto.PaymentStatus;
import com.jaz.movie.booking.entity.BookedTicket;
import com.jaz.movie.booking.service.PaymentService;
import com.jaz.movie.booking.utils.PaymentConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    private PaytmConfig paytmConfig;

    @Autowired
    private Environment env;

    /**
     * After completing the partial booking please hit this API to see the payment sign in page
     *
     * @return
     */
    @GetMapping("/login")
    public String toLogin() {
        return "login";
    }

    /**
     * Payment Redirection UI screen
     *
     * @return
     */
    @GetMapping("/payment-UI")
    public String home() {
        return "home";
    }

    /**
     * Once the Payment button clicked by the user it will redirect to this API and call back URL will handle the PAYTM gatewat.
     *
     * @param customerId        Holds the user id
     * @param transactionAmount Holds the amount
     * @param bookingId         Holds the partial booking id for validation
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/submit-payment")
    public ModelAndView submitPayment(@RequestParam(name = "CUST_ID") String customerId,
                                      @RequestParam(name = "TXN_AMOUNT") String transactionAmount,
                                      @RequestParam(name = "BOOKING_ID") String bookingId) throws Exception {
        // Check the ticket session in minutes
        paymentService.assertCheckTicketSession(Long.valueOf(bookingId));
        ModelAndView modelAndView = new ModelAndView("redirect:" + paytmConfig.getPaytmUrl());
        TreeMap<String, String> parameters = new TreeMap<>();
        paytmConfig.getDetails().forEach((k, v) -> parameters.put(k, v));
        parameters.put("MOBILE_NO", env.getProperty("paytm.mobile"));
        parameters.put("EMAIL", env.getProperty("paytm.email"));
        parameters.put("ORDER_ID", bookingId);
        parameters.put("TXN_AMOUNT", transactionAmount);
        parameters.put("CUST_ID", customerId);
        String checkSum = paymentService.getCheckSum(parameters);
        parameters.put("CHECKSUMHASH", checkSum);
        modelAndView.addAllObjects(parameters);
        return modelAndView;
    }

    /**
     * Once the payment was done by the user, It will redirect to here and check the sumhash and validat it
     * Update the status of the transaction and validate in our system
     * Trigger the email queue to send the ticket booked confirmation with QR code.
     *
     * @param request
     * @param model
     * @return
     */
    @PostMapping(value = "/validate-payment")
    public String validatePayment(HttpServletRequest request, Model model) {
        Map<String, String[]> mapData = request.getParameterMap();
        TreeMap<String, String> parameters = new TreeMap<>();
        String paytmChecksum = "";
        for (Map.Entry<String, String[]> requestParamsEntry : mapData.entrySet()) {
            if ("CHECKSUMHASH".equalsIgnoreCase(requestParamsEntry.getKey())) {
                paytmChecksum = requestParamsEntry.getValue()[0];
            } else {
                parameters.put(requestParamsEntry.getKey(), requestParamsEntry.getValue()[0]);
            }
        }
        BookedTicket bookedTicket = new BookedTicket();
        boolean isValideChecksum;
        log.info("RESULT : " + parameters.toString());
        try {
            isValideChecksum = paymentService.validateCheckSum(parameters, paytmChecksum);
            if (isValideChecksum && parameters.containsKey("RESPCODE")) {
                if (parameters.get("RESPCODE").equals("01")) {
                    log.info("updating the success payment in DB Booking Id: " + parameters.get("ORDERID"));
                    bookedTicket = paymentService.updatePayment(parameters, Long.valueOf(parameters.get("ORDERID")));
                } else {
                    paymentService.savePayment(parameters);
                    bookedTicket.setPaymentStatus(PaymentStatus.FAILURE.toString());
                }
            } else {
                paymentService.savePayment(parameters);
                bookedTicket.setPaymentStatus(PaymentStatus.FAILURE.toString());
            }
        } catch (Exception e) {
            log.info("Payment Failure ! : ", request);
            bookedTicket.setPaymentStatus(PaymentStatus.FAILURE.toString());
            paymentService.savePayment(parameters);
        }
        if (parameters.get(PaymentConstants.STATUS).equalsIgnoreCase(PaymentConstants.PAYMENT_SUCCESS)) {
            parameters.put("Movie Time", bookedTicket.getMovieTime());
            parameters.put("Booking Id", bookedTicket.getBookedSeats());
            parameters.put("Movie Name", bookedTicket.getMovieName());
            parameters.put("No of Persons", String.valueOf(bookedTicket.getNoOfPersons()));
        }
        model.addAttribute("parameters", parameters);
        return "report";
    }

}
