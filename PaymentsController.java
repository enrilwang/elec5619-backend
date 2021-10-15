//package net.guides.springboot2.springboot2webappjsp.controllers;
//
//import net.guides.springboot2.springboot2webappjsp.domain.Payments;
//import net.guides.springboot2.springboot2webappjsp.repositories.PaymentsRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
////api test json
////{
////        "paymentSerialId": "1",
////        "cardNumber": "1234567",
////        "cvv": "123",
////        "holderName": "Test",
////        "validTime": "2021-10-09 21:20:17"
////        }
//
//@RestController
//public class PaymentsController {
//    private static final String path = "api/payments";
//
//    @Autowired
//    PaymentsRepository paymentsRepo;
//
//    //C
//    @PostMapping(path)
//    public Result postPayments(@RequestBody Payments payment) {
//        Result result = new Result();
//        Payments paymentsQueryResult = null;
//
//        //check id in para is exist or not
//        paymentsQueryResult = paymentsRepo.getPaymentsByPaymentsSerialId(payment.getPaymentsSerialId());
//        if (paymentsQueryResult != null) {
//            result.setCode(400);
//            result.setMsg("Serial No duplicated.");
//            return result;
//        }
//
//        //check required data is provided
//        if (payment.getCardNumber() == null || payment.getCvv() == null || payment.getHolderName() == null || payment.getValidTime() == null) {
//            result.setCode(400);
//            result.setMsg("All fields should have values.");
//            return result;
//        }
//
//        //validate required data
//        //cvv
//        if (payment.getCvv().length() != 3) {
//            result.setMsg("CVV should have 3 digits");
//            return result;
//        } else {
//            for (int i = 0; i < 3; i++) {
//                //ASCII of 0 is 48, and that of 9 is 57
//                if (payment.getCvv().charAt(i) < 48 || payment.getCvv().charAt(i) > 57) {
//                    result.setMsg("CVV should be numbers");
//                    return result;
//                }
//            }
//
//        }
//        //cardnumber
//        for (int i = 0; i < payment.getCardNumber().length(); i++) {
//            //ASCII of 0 is 48, and that of 9 is 57
//            if (payment.getCardNumber().charAt(i) < 48 || payment.getCardNumber().charAt(i) > 57) {
//                result.setMsg("Card number should be numbers");
//                return result;
//            }
//        }
//        //validtime??
//
//        //format date
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String payTime = sdf.format(new Date());
//        Payments newPayment = new Payments(payTime, payment.getCardNumber(), payment.getCvv(), payment.getHolderName(), payment.getValidTime());
//
//        //db
//        paymentsQueryResult = paymentsRepo.save(newPayment);
//        if (paymentsQueryResult == null) {
//            result.setMsg("Save payment information error");
//            return result;
//        }
//        result.setMsg("Success");
//        result.setCode(201);
//        return result;
//
//    }
//
//    //U
//
//    //R
//    @GetMapping(path)
//    public Result getPayments(@RequestParam int paymentsSerialId) {
//        Result result = new Result();
//        Payments queryResult = paymentsRepo.getPaymentsByPaymentsSerialId(paymentsSerialId);
//        if (queryResult == null) {
//            result.setCode(400);
//            result.setMsg("No such payment record found.");
//            return result;
//        }
//        result.setCode(200);
//        result.setData(queryResult);
//        result.setMsg("Success");
//        return result;
//    }
//
//    //D
//
//}
