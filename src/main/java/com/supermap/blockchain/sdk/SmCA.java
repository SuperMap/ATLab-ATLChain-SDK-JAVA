package com.supermap.blockchain.sdk;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.GenerateCRLException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric_ca.sdk.exception.RevocationException;

import java.util.Date;

/**
 * Supermap CA 接口
 */
public interface SmCA {
    /**
     * 注册用户
     * @param user 注册用户
     * @param adminUser 管理员用户
     * @return 注册密码，用于获取密钥
     * @throws Exception
     */
    String register(SmUser user, SmUser adminUser) throws Exception;

    /**
     * 登记用户，注册完成后可以登记用户，登记之后可获得用户密钥
     * @param userName 用户名
     * @param secret 注册密码
     * @return 登记信息
     * @throws EnrollmentException
     * @throws InvalidArgumentException
     */
    Enrollment enroll(String userName, String secret) throws EnrollmentException, InvalidArgumentException;

    /**
     * 重登记注销登记的用户
     * @param user 重注册用户
     * @return 登记信息
     * @throws EnrollmentException
     * @throws InvalidArgumentException
     */
    Enrollment reenroll(SmUser user) throws EnrollmentException, InvalidArgumentException;

    /**
     * 注销用户，注销后该用户名永久失效
     * @param userName 用户名
     * @param reason 注销原因
     * @param adminUser 管理员用户
     * @return CRL
     * @throws InvalidArgumentException
     * @throws RevocationException
     */
    String revoke(String userName, String reason, SmUser adminUser) throws InvalidArgumentException, RevocationException;

    /**
     * 撤销登记，该用户可以重登记
     * @param enrollment 登记信息
     * @param reason 撤销原因
     * @param adminUser 管理员用户
     * @return CRL
     * @throws InvalidArgumentException
     * @throws RevocationException
     */
    String revoke(Enrollment enrollment ,String reason, SmUser adminUser) throws InvalidArgumentException, RevocationException;

    /**
     * 获取CRL
     * @param adminUser 管理员
     * @param revokedBefore 该日期之前撤销
     * @param revokedAfter 该日期之后撤销
     * @param expireBefore 该日期之前过期
     * @param expireAfter 该日期之前过期
     * @return
     * @throws GenerateCRLException
     * @throws InvalidArgumentException
     */
    String getCRL(User adminUser, Date revokedBefore, Date revokedAfter, Date expireBefore, Date expireAfter) throws GenerateCRLException, InvalidArgumentException;
}
