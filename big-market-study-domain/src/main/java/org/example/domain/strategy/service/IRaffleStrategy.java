package org.example.domain.strategy.service;

import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactoryEntity;

public interface IRaffleStrategy {
    RaffleAwardEntity performRaffle(RaffleFactoryEntity raffleFactoryEntity);
}
