package wrapper;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @program: ims-business
 * @description: 自定义Wrapper，用户读取、解析POST请求的body，同时防止request.getInputStream()只能读一次后为空
 * @author: lidong
 * @create: 2020/04/08
 */
public class PostParamRequestWrapper extends HttpServletRequestWrapper {

    private String requestBody;
    private Charset charSet;

    public PostParamRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        requestBody = getRequestPostStr(request);
    }

    /**
     * 获取POST请求参数字符串表述
     *
     * @param request
     * @return
     * @throws IOException
     */
    public String getRequestPostStr(HttpServletRequest request) throws IOException {
        String charSetStr = request.getCharacterEncoding();
        if (charSetStr == null) {
            charSetStr = "UTF-8";
        }
        charSet = Charset.forName(charSetStr);

        return StreamUtils.copyToString(request.getInputStream(), charSet);
    }

    /**
     * 重写 getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    /**
     * 重写 getReader()
     */
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * 获取请求体
     *
     * @return
     */
    public String getBody() {
        return this.requestBody;
    }
}
