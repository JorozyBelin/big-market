package org.example.domain.credit.service;

import org.example.domain.credit.model.entity.TradeEntity;

public interface ICreditAdjustService {

    String createOrder(TradeEntity tradeEntity);
}
