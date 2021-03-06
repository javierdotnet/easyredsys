package com.miguelangeljulvez.easyredsys.client.core;


import com.miguelangeljulvez.easyredsys.client.AppConfig;
import com.miguelangeljulvez.easyredsys.client.util.Currency;
import com.miguelangeljulvez.easyredsys.client.util.Language;
import com.miguelangeljulvez.easyredsys.client.util.PaymentMethod;
import com.miguelangeljulvez.easyredsys.client.util.TransactionType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
** Orden de compra usando compra CES sin usar servicios web
 */
public final class OrderCES extends Order {

    protected OrderCES() {
        //Inicializamos por no tocar las librerías de redsys
        apiMacSha256.setParameter("DS_MERCHANT_MERCHANTURL", "");
        apiMacSha256.setParameter("DS_MERCHANT_URLOK", "");
        apiMacSha256.setParameter("DS_MERCHANT_URLKO", "");
        apiMacSha256.setParameter("DS_MERCHANT_NAME", "");
        apiMacSha256.setParameter("DS_CONSUMER_LANGUAGE", "");
        apiMacSha256.setParameter("DS_MERCHANT_PAYMETHODS", "");
    }

    public String getDs_merchant_merchantURL() {
        return apiMacSha256.getParameter("DS_MERCHANT_MERCHANTURL");
    }

    public void setDs_merchant_merchantURL(String ds_merchant_merchantURL) {
        apiMacSha256.setParameter("DS_MERCHANT_MERCHANTURL", ds_merchant_merchantURL);
    }

    public String getDs_merchant_UrlOK() {
        return apiMacSha256.getParameter("DS_MERCHANT_URLOK");
    }

    public void setDs_merchant_UrlOK(String ds_merchant_UrlOK) {
        apiMacSha256.setParameter("DS_MERCHANT_URLOK", ds_merchant_UrlOK);
    }

    public String getDs_merchant_UrlKO() {
        return apiMacSha256.getParameter("DS_MERCHANT_URLKO");
    }

    public void setDs_merchant_UrlKO(String ds_merchant_UrlKO) {
        apiMacSha256.setParameter("DS_MERCHANT_URLKO", ds_merchant_UrlKO);
    }

    public String getDs_merchant_merchantName() {
        return apiMacSha256.getParameter("DS_MERCHANT_NAME");
    }

    public void setDs_merchant_merchantName(String ds_merchant_merchantName) {
        apiMacSha256.setParameter("DS_MERCHANT_NAME", ds_merchant_merchantName);
    }

    public int getDs_merchant_consumerLanguage() {
        try {
            return Integer.valueOf(apiMacSha256.getParameter("DS_CONSUMER_LANGUAGE"));
        } catch (NumberFormatException nef) {
        }

        return 0;
    }

    public void setDs_merchant_consumerLanguage(int ds_merchant_consumerLanguage) {
        apiMacSha256.setParameter("DS_CONSUMER_LANGUAGE", String.valueOf(ds_merchant_consumerLanguage));
    }

    public String getDs_merchant_paymethods() {
        return apiMacSha256.getParameter("DS_MERCHANT_PAYMETHODS");
    }

    public void setDs_merchant_paymethods(String ds_merchant_paymethods) {
        apiMacSha256.setParameter("DS_MERCHANT_PAYMETHODS", ds_merchant_paymethods);
    }

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();

        sb.append(super.toString());

        sb.append("DS_MERCHANT_MERCHANTURL:");
        sb.append(getDs_merchant_merchantURL());
        sb.append(System.lineSeparator());
        sb.append("DS_MERCHANT_URLOK:");
        sb.append(getDs_merchant_UrlOK());
        sb.append(System.lineSeparator());
        sb.append("DS_MERCHANT_URLKO:");
        sb.append(getDs_merchant_UrlKO());
        sb.append(System.lineSeparator());
        sb.append("DS_MERCHANT_NAME:");
        sb.append(getDs_merchant_merchantName());
        sb.append(System.lineSeparator());
        sb.append("DS_CONSUMER_LANGUAGE:");
        sb.append(getDs_merchant_consumerLanguage());
        sb.append(System.lineSeparator());

        return sb.toString();
    }

    public static class Builder {

        private long merchantCode;
        private long terminal;
        private String transactionType;
        private long currency;
        private int consumerLanguage;
        private String order;
        private long amount;
        private String urlOk;
        private String urlKo;
        private String urlNotification;
        private String productDescription;
        private String payMethods;

        public Builder() {}

        public Builder(Class<? extends AppConfig> userActionClass) {
            try {
                Method getMerchantCode = userActionClass.getDeclaredMethod("getMerchantCode");
                getMerchantCode.setAccessible(true);
                Method getTerminal = userActionClass.getDeclaredMethod("getTerminal");
                getTerminal.setAccessible(true);

                this.merchantCode = Long.valueOf((String) getMerchantCode.invoke(null));
                this.terminal = Long.valueOf((String) getTerminal.invoke(null));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        public Builder merchantCode(final String merchantCode) {
            this.merchantCode = Long.valueOf(merchantCode);
            return this;
        }

        public Builder terminal(final String terminal) {
            this.terminal = Long.valueOf(terminal);
            return this;
        }

        public Builder transactionType(final TransactionType transactionType) {
            this.transactionType = transactionType.getCode();
            return this;
        }

        public Builder currency(final Currency currency) {
            this.currency = currency.getISOCodeNumeric();
            return this;
        }

        public Builder consumerLanguage(final Language consumerLanguage) {
            this.consumerLanguage = Integer.valueOf(consumerLanguage.getCode());
            return this;
        }

        public Builder order(final String order) {
            this.order = order;
            return this;
        }

        public Builder amount(final long amount) {
            this.amount = amount;
            return this;
        }

        public Builder urlOk(final String urlOk) {
            this.urlOk = urlOk;
            return this;
        }

        public Builder urlKo(final String urlKo) {
            this.urlKo = urlKo;
            return this;
        }

        public Builder urlNotification(final String urlNotification) {
            this.urlNotification = urlNotification;
            return this;
        }

        public Builder productDescription(final String productDescription) {
            this.productDescription = productDescription;
            return this;
        }

        public Builder payMethods(final PaymentMethod paymentMethods) {
            this.payMethods = paymentMethods.getCode();
            return this;
        }

        public OrderCES build() {
            OrderCES orderCES =  new OrderCES();
            orderCES.setDs_merchant_merchantcode(merchantCode);
            orderCES.setDs_merchant_terminal(terminal);
            orderCES.setDs_merchant_transactiontype(transactionType);
            orderCES.setDs_merchant_currency(currency);
            orderCES.setDs_merchant_consumerLanguage(consumerLanguage);
            orderCES.setDs_merchant_order(order);
            orderCES.setDs_merchant_amount(amount);
            orderCES.setDs_merchant_merchantURL(urlNotification);
            orderCES.setDs_merchant_UrlOK(urlOk);
            orderCES.setDs_merchant_UrlKO(urlKo);
            orderCES.setDs_merchant_productdescription(productDescription);
            orderCES.setDs_merchant_paymethods(payMethods);

            //TODO - Lanzar error de validación

            return orderCES;
        }
    }
}
