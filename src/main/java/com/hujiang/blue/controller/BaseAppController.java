package com.hujiang.blue.controller;

import com.hujiang.java.common.utility.exception.*;
import com.hujiang.java.common.utility.model.Result;
import com.hujiang.java.common.utility.model.ResultBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

import static com.hujiang.java.common.utility.constant.CommonStatusCode.REQUEST_PARAMS_INVALID_ERROR;


public class BaseAppController extends BaseController {

    /**
     * 用于处理通用异常
     *
     * @return
     */
    @CrossOrigin("*")
    @ResponseBody
    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exception(Exception e) throws IOException {
        //log.warn("got a Exception",e);

        Integer status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = e.getMessage();

        return handleValue(status, message);
    }


    /**
     * 用于处理通用异常
     *
     * @return
     */
    @CrossOrigin("*")
    @ResponseBody
    @ExceptionHandler({ RuntimeException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exception(RuntimeException e) throws IOException {
        //log.warn("got a Exception",e);

        Integer status = HttpStatus.BAD_REQUEST.value();
        String message = e.getMessage();

        return handleValue(status, message);
    }

    /**
     * 用于处理CacheException
     *
     * @param e
     * @return
     */
    @CrossOrigin("*")
    @ResponseBody
    @ExceptionHandler({ CacheException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exception(CacheException e) throws IOException {
        //log.warn("got a UnauthorizedException",e);

        String message = e.getMessage();
        Integer status = e.getCode();

        return handleValue( status, message);
    }


    /**
     * 用于处理InternalServiceException
     *
     * @param e
     * @return
     */
    @CrossOrigin("*")
    @ResponseBody
    @ExceptionHandler({ CommonException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Result exception(CommonException e) throws IOException {
        //log.warn("got a Exception",e);

        Integer status = e.getCode();
        String message = e.getMessage();

        return handleValue(status, message);
    }

    /**
     * 用于处理ConfigurationException
     *
     * @param e
     * @return
     */
    @CrossOrigin("*")
    @ResponseBody
    @ExceptionHandler({ ConfigurationException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exception(ConfigurationException e) throws IOException {
        //log.warn("got a Exception",e);

        Integer status = e.getCode();
        String message = e.getMessage();

        return handleValue(status, message);
    }

    /**
     * 用于处理DataBaseException
     *
     * @param e
     * @return
     */
    @CrossOrigin("*")
    @ResponseBody
    @ExceptionHandler({ DataBaseException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exception(DataBaseException e) throws IOException {
        //log.warn("got a UnauthorizedException",e);

        String message = e.getMessage();
        Integer status = e.getCode();

        return handleValue( status, message);
    }

    /**
     * 用于处理HealthcheckException
     * @param e
     * @return
     */
    @CrossOrigin("*")
    @ResponseBody
    @ExceptionHandler({HealthcheckException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Result exception(HealthcheckException e) throws IOException {

        String message = e.getMessage();
        Integer status = e.getCode();

        return handleValue(status, message);
    }

    /**
     * 用于处理InternalServiceException
     *
     * @param e
     * @return
     */
    @CrossOrigin("*")
    @ResponseBody
    @ExceptionHandler({ InternalServiceException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exception(InternalServiceException e) throws IOException {
        //log.warn("got a Exception",e);

        Integer status = e.getCode();
        String message = e.getMessage();

        return handleValue(status, message);
    }

    /**
     * 用于处理RestException
     *
     * @param e
     * @return
     */
    @CrossOrigin("*")
    @ResponseBody
    @ExceptionHandler({ RestException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Result exception(RestException e) throws IOException {
        //log.warn("got a UnauthorizedException",e);

        String message = e.getMessage();
        Integer status = e.getCode();

        return handleValue( status, message);

    }

    /**
     * 用于处理UnauthorizedException
     *
     * @param e
     * @return
     */
    @CrossOrigin("*")
    @ResponseBody
    @ExceptionHandler({ UnauthorizedException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Result exception(UnauthorizedException e) throws IOException {
        //log.warn("got a UnauthorizedException",e);

        String message = e.getMessage();
        Integer status = e.getCode();

        return handleValue( status, message);

    }

    /**
     * 用于处理HttpMessageNotReadableException
     *
     * @param e
     * @return
     */
    @CrossOrigin("*")
    @ResponseBody
    @ExceptionHandler({ HttpMessageNotReadableException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Result exception(HttpMessageNotReadableException e) throws IOException {
        //log.warn("got a Exception",e);

        Integer status = REQUEST_PARAMS_INVALID_ERROR.getStatus();
        String message = REQUEST_PARAMS_INVALID_ERROR.getMessage();

        return handleValue(status, message);
    }

    private Result handleValue(int status, String message) throws IOException {
        return (new ResultBuilder<>()).status(status).message(message).build();
    }
    protected <T> Result<T> buildResult(T obj) {
        return (new ResultBuilder<T>()).status(0).message("OK").data(obj).build();
    }

    protected String result(String data) throws IOException {
        if(null == data) {
            throw new RestException();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n").append(" \"status\" : 0,\n").append(" \"message\" : \"OK\",\n").append(" \"data\" : ").append(data).append("\n").append("}");
            return sb.toString();
        }
    }
}
