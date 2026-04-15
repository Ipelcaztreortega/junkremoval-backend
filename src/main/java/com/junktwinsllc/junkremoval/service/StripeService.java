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

    // Creates a Stripe payment link for a job
    // The customer clicks this link and pays via card, Apple Pay, Google Pay etc.
    public String createPaymentLink(Job job, String customerEmail) throws Exception {
        Stripe.apiKey = secretKey;

        // Amount in cents — Stripe always uses smallest currency unit
        long amountInCents = job.getAgreedPrice()
                .multiply(new java.math.BigDecimal("100"))
                .longValue();

        // Create a product in Stripe representing this job
        Product product = Product.create(
                ProductCreateParams.builder()
                        .setName("JunkTwins — Job " + job.getContractNumber())
                        .build()
        );

        // Create a price for that product
        Price price = Price.create(
                PriceCreateParams.builder()
                        .setProduct(product.getId())
                        .setUnitAmount(amountInCents)
                        .setCurrency("usd")
                        .build()
        );

        // Build the payment link
        PaymentLinkCreateParams.Builder params = PaymentLinkCreateParams.builder()
                .addLineItem(
                        PaymentLinkCreateParams.LineItem.builder()
                                .setPrice(price.getId())
                                .setQuantity(1L)
                                .build()
                )
                // Store the job ID so the webhook knows which job to mark paid
                .putMetadata("jobId", String.valueOf(job.getId()));

        // Pre-fill customer email if provided
        if (customerEmail != null && !customerEmail.isBlank()) {
            params.setCustomerCreation(PaymentLinkCreateParams.CustomerCreation.ALWAYS);
        }

        PaymentLink link = PaymentLink.create(params.build());
        return link.getUrl();
    }
}