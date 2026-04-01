package org.example.domain.activity.service.product;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.SkuProductEntity;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.IRaffleActivitySkuProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RaffleActivitySkuProductService implements IRaffleActivitySkuProductService {
    @Autowired
    private IActivityRepository activityRepository;
    @Override
    public List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId) {
        return activityRepository.querySkuProductEntityListByActivityId(activityId);
    }
}
