package com.swapnil.test.service.impl;

import com.swapnil.test.dto.ExchangeRate;
import com.swapnil.test.util.FileLoader;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExchangeServiceImplTest {

    @Mock
    FileLoader fileLoader;


    ExchangeRateServiceImpl exchangeService;

    public ExchangeServiceImplTest() {

        exchangeService = new ExchangeRateServiceImpl();
        fileLoader = Mockito.mock(FileLoader.class);
        exchangeService.setFileLoader(fileLoader);
    }


    @Test
    public void getExchangeRateOfDayTest() throws Exception{
        List<ExchangeRate> result = getResult();
        Mockito.when(fileLoader.loadFile("2018-01-01")).thenReturn(result);
        List<ExchangeRate> list = exchangeService.getExchangeRateOfDay("2018-01-01");
        //Mockito.verify()
        Assert.assertEquals(list.size(),1);
        Assert.assertEquals(list.get(0).getCurrency(),"SGD");
        Assert.assertEquals(list.get(0).getRate(),new BigDecimal("0.73"));
        Assert.assertEquals(list.get(0).getBaseCurrency(),"USD");
        Assert.assertEquals(list.get(0).getDate(),"2018-01-01");

    }

    @Test
    public void  findRateTest() throws Exception{
        List<ExchangeRate> result = getResult();
        ExchangeRate rate = new ExchangeRate();
        rate.setDate("2018-01-01");
        rate.setCurrency("INR");
        rate.setBaseCurrency("USD");
        rate.setRate(new BigDecimal("0.015"));
        result.add(rate);
        Mockito.when(fileLoader.loadFile("2018-01-01")).thenReturn(result);
        ExchangeRate exchangeRate = exchangeService.findRate("2018-01-01","SGD","INR");

        Assert.assertNotEquals(exchangeRate,null);
        Assert.assertEquals(exchangeRate.getCurrency(),"INR");
        Assert.assertEquals(exchangeRate.getRate(),new BigDecimal("48.67"));
        Assert.assertEquals(exchangeRate.getBaseCurrency(),"SGD");
        Assert.assertEquals(exchangeRate.getDate(),"2018-01-01");
    }


    @Test
    public void findAll() throws Exception{
        List<ExchangeRate> result0101 = getResult();
        Mockito.when(fileLoader.loadFile("2018-01-01")).thenReturn(result0101);
        List<ExchangeRate> result0201 = getResult();
        ExchangeRate rate = new ExchangeRate();
        rate.setDate("2018-01-02");
        rate.setCurrency("SGD");
        rate.setBaseCurrency("USD");
        rate.setRate(new BigDecimal("0.75"));
        result0201.add(rate);
        Mockito.when(fileLoader.loadFile("2018-01-02")).thenReturn(result0201);
        List<ExchangeRate> list = exchangeService.findAll("2018-01-01","2018-01-02","SGD");

        Assert.assertEquals(list.size(),2);
        Assert.assertEquals(list.get(0).getCurrency(),"SGD");
        Assert.assertEquals(list.get(0).getRate(),new BigDecimal("0.73"));
        Assert.assertEquals(list.get(0).getBaseCurrency(),"USD");
        Assert.assertEquals(list.get(0).getDate(),"2018-01-01");

        Assert.assertEquals(list.get(1).getCurrency(),"SGD");
        Assert.assertEquals(list.get(1).getRate(),new BigDecimal("0.75"));
        Assert.assertEquals(list.get(1).getBaseCurrency(),"USD");
        Assert.assertEquals(list.get(1).getDate(),"2018-01-02");
    }

    private List<ExchangeRate> getResult(){
        List<ExchangeRate> result = new ArrayList<ExchangeRate>();
        ExchangeRate rate = new ExchangeRate();
        rate.setDate("2018-01-01");
        rate.setCurrency("SGD");
        rate.setBaseCurrency("USD");
        rate.setRate(new BigDecimal("0.73"));
        result.add(rate);
        return result;
    }




}
