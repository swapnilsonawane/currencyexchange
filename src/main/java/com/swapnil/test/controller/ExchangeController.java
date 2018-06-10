package com.swapnil.test.controller;

import com.swapnil.test.dto.ExchangeRate;
import com.swapnil.test.dto.ResponseDto;
import com.swapnil.test.service.ExchangeService;
import com.swapnil.test.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {


    @Autowired
    private ResponseHandler responseHandler;

    @Autowired
    private ExchangeService exchangeService;

    @RequestMapping(value = "hello")
    public String hello(){
        return "Hello Exchanger ....";
    }


    @RequestMapping(value = "rateofday")
    public ResponseDto<List<ExchangeRate>> getExchangeRateOfTheDay(@RequestParam("date") String date) {
        try {
            List<ExchangeRate> list = exchangeService.getExchangeRateOfDay(date);
            return responseHandler.getSuccessResponse(list);
        } catch (Exception e){
            return responseHandler.getErrorResponse(e.getMessage());
        }


    }

    @RequestMapping(value = "rate")
    public ResponseDto<ExchangeRate> findRate(@RequestParam("date") String date,@RequestParam("baseCurrency") String baseCurrency,
                                                           @RequestParam("currency") String currency) {

        try {
            ExchangeRate exchangeRate = exchangeService.findRate(date,baseCurrency,currency);
            return responseHandler.getSuccessResponse(exchangeRate);
        } catch (Exception e){
            return responseHandler.getErrorResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "rates")
    public ResponseDto<List<ExchangeRate>> findRatesForDates(@RequestParam("startDate") String startDate,
                                                              @RequestParam("endDate") String endDate,@RequestParam("currency") String currency) {
        try {
            List<ExchangeRate> list = exchangeService.findAll(startDate,endDate,currency);
            return responseHandler.getSuccessResponse(list);
        } catch (Exception e){
            return responseHandler.getErrorResponse(e.getMessage());
        }
    }
}
