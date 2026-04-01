package org.example.domain.credit.service;

import org.example.domain.credit.model.entity.CreditAccountEntity;
import org.example.domain.credit.model.entity.TradeEntity;

public interface ICreditAdjustService {

    String createOrder(TradeEntity tradeEntity);

    /**
     * 查询用户信用账户
     * @param userId
     * @return
     */
    CreditAccountEntity queryUserCreditAccount(String userId);
}
