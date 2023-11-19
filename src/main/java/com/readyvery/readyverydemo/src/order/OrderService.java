package com.readyvery.readyverydemo.src.order;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.order.dto.CartAddReq;
import com.readyvery.readyverydemo.src.order.dto.CartAddRes;
import com.readyvery.readyverydemo.src.order.dto.CartEditReq;
import com.readyvery.readyverydemo.src.order.dto.CartEidtRes;
import com.readyvery.readyverydemo.src.order.dto.CartItemDeleteReq;
import com.readyvery.readyverydemo.src.order.dto.CartItemDeleteRes;
import com.readyvery.readyverydemo.src.order.dto.CartResetRes;
import com.readyvery.readyverydemo.src.order.dto.FoodyDetailRes;

public interface OrderService {
	FoodyDetailRes getFoody(Long storeId, Long foodyId, Long inout);

	CartAddRes addCart(CustomUserDetails userDetails, CartAddReq cartAddReq);

	CartEidtRes editCart(CustomUserDetails userDetails, CartEditReq cartEditReq);

	CartItemDeleteRes deleteCart(CustomUserDetails userDetails, CartItemDeleteReq cartItemDeleteReq);

	CartResetRes resetCart(CustomUserDetails userDetails);
}
