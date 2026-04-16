package com.junktwinsllc.junkremoval.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.junktwinsllc.junkremoval.model.Job;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    public String createPaymentLink(Job job, String customerEmail) throws Exception {
        Stripe.apiKey = secretKey;

        long amountInCents = job.getAgreedPrice()
                .multiply(new java.math.BigDecimal("100"))
                .longValue();

        Product product = Product.create(
                ProductCreateParams.builder()
                        .setName("JunkTwins — Job " + job.getContractNumber())
                        .build()
        );

        Price price = Price.create(
                PriceCreateParams.builder()
                        .setProduct(product.getId())
                        .setUnitAmount(amountInCents)
                        .setCurrency("usd")
                        .build()
        );

        PaymentLink link = PaymentLink.create(
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId())
                                        .setQuantity(1L)
                                        .build()
                        )
                        // jobId in metadata so webhook can identify which job to mark paid
                        .putMetadata("jobId", String.valueOf(job.getId()))
                        // Collect metadata at checkout session level
                        .setPaymentIntentData(
                                PaymentLinkCreateParams.PaymentIntentData.builder()
                                        .putMetadata("jobId", String.valueOf(job.getId()))
                                        .build()
                        )
                        .build()
        );

        return link.getUrl();
    }
}