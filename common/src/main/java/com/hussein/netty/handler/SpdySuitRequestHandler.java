package com.hussein.netty.handler;

/**
 * <p>Title: SpdySuitRequestHandler</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/7/8 11:00 AM
 */
public class SpdySuitRequestHandler extends HttpSuitRequestHandler {

    @Override
    protected String getContent() {
        return "This content is transmitted via SPDY\r\n";
    }
}
