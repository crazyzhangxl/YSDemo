/**
 * @ProjectName: 民用软件平台软件
 * @Copyright: 2012 HangZhou Hikvision System Technology Co., Ltd. All Right Reserved.
 * @address: http://www.hikvision.com
 * @date: 2014-6-6 上午8:57:54
 * @Description: 本内容仅限于杭州海康威视数字技术股份有限公司内部使用，禁止转发.
 */
package com.videogo.remoteplayback.list.querylist;

import com.videogo.openapi.bean.resp.CloudPartInfoFile;
import com.videogo.remoteplayback.list.bean.CloudPartInfoFileEx;

import java.util.List;

/**
 * <p>
 * 获取回放列表回调接口
 * </p>
 * 
 * @author hanlifeng 2014-6-6 上午8:57:54
 * @version V2.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-6-6
 * @modify by reason:{方法名}:{原因}
 */
public interface QueryPlayBackListTaskCallback {

    /**
     * <p>
     * 云+设备本地 无录像数据
     * </p>
     * 
     * @author hanlifeng 2014-6-9 上午10:44:11
     */
    void queryHasNoData();

    /**
     * <p>
     * 无云 + 有设备本地
     * </p>
     * 
     * @author hanlifeng 2014-6-9 上午10:48:58
     */
    void queryOnlyHasLocalFile();

    /**
     * <p>
     * 按月查只有本地，具体查询本地确无数据
     * </p>
     * 
     * @author hanlifeng 2014-6-17 上午9:21:09
     */
    void queryOnlyLocalNoData();

    /**
     * <p>
     * 查本地异常
     * </p>
     * 
     * @author hanlifeng 2014-6-17 上午9:22:05
     */
    void queryLocalException();


    void queryCloudSucess(List<CloudPartInfoFileEx> cloudPartInfoFileEx, int queryMLocalStatus, List<CloudPartInfoFile> cloudPartInfoFile);

    void queryLocalSucess(List<CloudPartInfoFileEx> cloudPartInfoFileEx, int position, List<CloudPartInfoFile> cloudPartInfoFile);

    /**
     * <p>
     * 本地无数据
     * </p>
     * 
     * @author hanlifeng 2014-6-19 下午2:03:38
     */
    void queryLocalNoData();

    /**
     * <p>
     * 异常处理
     * </p>
     * 
     * @author hanlifeng 2014-6-9 上午10:54:05
     */
    void queryException();

    /**
     * <p>
     * 查询录像数据结束
     * </p>
     * 
     * @author hanlieng 2014-8-6 下午3:02:14
     * @param type
     * @param queryMode
     *            查询结果
     * @param queryErrorCode
     *            查询errorCode
     * @param detail
     */
    void queryTaskOver(int type, int queryMode, int queryErrorCode, String detail);

}
