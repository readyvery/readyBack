package com.readyvery.readyverydemo.src.order;

import com.readyvery.readyverydemo.src.order.dto.FoodyDetailRes;

public interface OrderService {
	FoodyDetailRes getFoody(Long storeId, Long foodyId, Long inout);
}
