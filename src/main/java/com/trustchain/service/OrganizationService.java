package com.trustchain.service;

import java.util.List;
import java.util.Map;

import com.mybatisflex.core.paginate.Page;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;


public interface OrganizationService {

    /**
     * 获取机构选择列表
     *
     * @return 机构选择列表
     */
    List<Organization> selectList();

    /**
     * 判断机构是否存在
     *
     * @param orgName 机构名称
     * @param orgId   机构ID
     * @return 是否存在
     */
    boolean exist(String orgName, String orgId);

    /**
     * 机构注册申请
     *
     * @param orgReg 机构注册对象
     * @return 注册申请号
     */
    String registerApply(OrganizationRegister orgReg);

    /**
     * 机构注册查询
     *
     * @param applyIds 注册申请号
     * @return 注册申请列表
     */
    List<OrganizationRegister> registerApplyList(List<String> applyIds);

    /**
     * @param applyId 注册申请号
     * @return 注册申请详情
     */
    OrganizationRegister registerApplyDetail(String applyId);

    /**
     * 获取注册申请列表
     *
     * @param orgId      机构ID
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param filter     过滤
     * @param sort       排序
     * @return 注册申请列表
     */
    Page<OrganizationRegister> registerApprovalList(String orgId,
                                                    Integer pageNumber,
                                                    Integer pageSize,
                                                    Map<String, List<String>> filter,
                                                    Map<String, String> sort);

    /**
     * @param applyId 注册申请号
     * @return 注册申请信息
     */
    OrganizationRegister registerApprovalDetail(String applyId);

    /**
     * 注册申请回复
     *
     * @param applyId 注册申请号
     * @param reply   回复类型
     * @param reason  回复理由
     * @return 成功与否
     */
    boolean registerReply(String applyId, ApplyStatus reply, String reason);

    /**
     * 机构注册
     *
     * @param org 机构
     * @return 是否插入成功
     */
    boolean register(Organization org);

    /**
     * 获取机构详情
     *
     * @param orgId   机构ID
     * @param version 版本号
     * @return 机构信息
     */
    Organization informationDetail(String orgId, String version);

    /**
     * @param organization 机构
     * @return
     */
    Organization informationUpdate(Organization organization);

    /**
     * 获取机构信息的历史记录
     *
     * @param orgId 机构ID
     * @return 机构信息历史记录
     */
    List<Organization> informationHistory(String orgId);

    /**
     * 机构信息回滚
     *
     * @param orgId   机构ID
     * @param version 版本号
     * @return 是否回滚成功
     */
    boolean informationRollback(String orgId, String version);

    /**
     * 获取机构的下级机构列表
     *
     * @param orgId 机构ID
     * @return 下级机构列表
     */
    List<Organization> subordinateList(String orgId);

    /**
     * 获取下级机构详情
     *
     * @param orgId 机构ID
     * @return 下级机构详情
     */
    Organization subordinateDetail(String orgId);
}
