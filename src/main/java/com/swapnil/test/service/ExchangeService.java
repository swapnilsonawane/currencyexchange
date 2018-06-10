package com.swapnil.test.service;

import com.swapnil.test.dto.ExchangeRate;

import java.util.List;

public interface ExchangeService {

    public List<ExchangeRate> getExchangeRateOfDay(String date) throws Exception;

    public ExchangeRate findRate(String date,String baseCurrency,String currency) throws Exception;

    public List<ExchangeRate> findAll(String startDate,String endDate,String currency) throws Exception;
}
