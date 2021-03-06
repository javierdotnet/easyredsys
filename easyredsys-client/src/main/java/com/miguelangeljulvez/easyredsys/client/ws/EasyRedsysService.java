package com.miguelangeljulvez.easyredsys.client.ws;

import com.miguelangeljulvez.easyredsys.client.AppConfig;
import com.miguelangeljulvez.easyredsys.client.OperationException;
import com.miguelangeljulvez.easyredsys.client.core.MessageOrderNoCESRequest;
import com.miguelangeljulvez.easyredsys.client.core.MessageOrderNoCESResponse;
import com.miguelangeljulvez.easyredsys.client.util.ErrorCodes;
import com.miguelangeljulvez.easyredsys.client.util.RedsysAddresses;
import com.miguelangeljulvez.easyredsys.client.util.ResponseCodes;
import com.miguelangeljulvez.easyredsys.client.util.XMLUtil;
import com.miguelangeljulvez.easyredsys.client.ws.client.SerClsWSEntrada;
import com.miguelangeljulvez.easyredsys.client.ws.client.SerClsWSEntradaService;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EasyRedsysService {

    private EasyRedsysService(){}

    public static synchronized MessageOrderNoCESResponse request(MessageOrderNoCESRequest messageOrderNoCESRequest, Class<? extends AppConfig> userActionClass) throws OperationException {

        AppConfig appConfig;
        try {
            appConfig = userActionClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            _log.log(Level.WARNING, "Error al instanciar la clase AppConfig. Debes crear una clase que implemente la interface AppConfig");

            throw new OperationException("ER-2", e.getMessage());
        }

        MessageOrderNoCESResponse messageOrderNoCESResponse = request(messageOrderNoCESRequest);

        appConfig.saveNotification(messageOrderNoCESResponse.getOperationNoCES());

        return messageOrderNoCESResponse;
    }

    protected static synchronized MessageOrderNoCESResponse request(MessageOrderNoCESRequest messageOrderNoCESRequest) throws OperationException {

        SerClsWSEntrada service;
        try {
            URL location = new URL(RedsysAddresses.getWebserviceURL(messageOrderNoCESRequest.getRedsysUrl()));
            SerClsWSEntradaService serClsWSEntradaService = new SerClsWSEntradaService(location);
            service = serClsWSEntradaService.getSerClsWSEntrada();
        } catch (Exception e) {
            _log.log(Level.WARNING, e.getMessage(), e);
            throw new OperationException("ER-0", e.getMessage());
        }

        String requestServiceXML = XMLUtil.toRedsysXML(messageOrderNoCESRequest);

        _log.log(Level.FINEST, "XML Request: " + requestServiceXML);

        String responseServiceXML = service.trataPeticion(requestServiceXML);

        _log.log(Level.FINEST, "XML Response: " + responseServiceXML);

        MessageOrderNoCESResponse messageOrderNoCESResponse = new MessageOrderNoCESResponse(responseServiceXML, messageOrderNoCESRequest.getClaveSecreta());

        switch (messageOrderNoCESResponse.getCodigo()) {
            case "0":
                break;
            default:
                _log.log(Level.WARNING, "OperationException: Código de error");

                throw new OperationException(messageOrderNoCESResponse.getCodigo(), ErrorCodes.getErrorMessage(messageOrderNoCESResponse.getCodigo()));
        }

        if (!messageOrderNoCESResponse.isValid()) {
            _log.log(Level.WARNING, "OperationException: La firma recibida por el servidor no es válida");

            throw new OperationException("ER-1", "La firma recibida por el servidor no es válida");
        }


        if (!ResponseCodes.isSuccessResponse(messageOrderNoCESResponse.getOperationNoCES().getDs_Response())) {
            _log.log(Level.WARNING, "OperationException: Response code de error");

            throw new OperationException(messageOrderNoCESResponse.getOperationNoCES().getDs_Response(), ResponseCodes.getErrorResponseMessage(messageOrderNoCESResponse.getOperationNoCES().getDs_Response()));
        }

        return messageOrderNoCESResponse;
    }

    private static final Logger _log = Logger.getLogger(EasyRedsysService.class.getName());
}
