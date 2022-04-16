package com.jindidata.user.redpacket.provider;

import com.jindidata.common.pojo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import static com.jindidata.common.pojo.ResultFactory.ok;

/**
 * 类: DemoProvider <br/>
 * 描述: 示例模块<br/>
 * 方法: 
 * <ul>
 * 	<li></li>
 * </ul>
 * @author wangfei
 */
@Api(tags={"示例"})
public interface DemoProvider {

    String PATH_PREFIX = "demo";

    @GetMapping("test")
    void test();

    @GetMapping("test2")
    void test2();

    @GetMapping("realCgid")
    @ApiOperation("根据cgid参数获取指定公司当前最新cgid")
    Result<Long> findRealCgid(@NotNull @RequestParam("cgid") Long cgid);

    @GetMapping("realCgid/forInnerRpc")
    @ApiOperation("根据cgid参数获取指定公司当前最新cgid")
    Long findRealCgidForInnerRpc(@NotNull @RequestParam("cgid") Long cgid);

    class DefaultFallback implements DemoProvider {
        @Override
        public void test() {
        }

        @Override
        public void test2() {

        }

        @Override
        public Result<Long> findRealCgid(@NotNull Long cgid) {
            return ok(cgid);
        }

        @Override
        public Long findRealCgidForInnerRpc(@NotNull Long cgid) {
            return cgid;
        }
    }
}

