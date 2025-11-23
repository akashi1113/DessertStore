package com.csu.userservice.mapper;

import com.csu.userservice.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AccountDao {

    // 注册邮箱账号
    int insertEmailAccount(@Param("email") String email,
                           @Param("username") String username,
                           @Param("password") String password);

    // GitHub 用户注册（插入 account 表）
    int insertGithubAccount(Account account);

    // GitHub 用户注册（插入 github_account 表）
    int insertGithub(@Param("userid") Long userid,
                     @Param("github_id") int githubId,
                     @Param("github_login") String githubLogin);

    // GitHub 查询
    Account searchGithubAccount(@Param("github_id") int githubId);

    // 登录
    Account login(@Param("email") String email,
                  @Param("password") String password);

    // 根据 userid 查询账号
    Account getAccountById(@Param("userid") Long userid);

    // 根据 email 查询账号
    Account getAccountByEmail(@Param("email") String email);

    // 更新账号资料
    int updateAccount(Account account);

    // 修改邮箱
    int resetEmail(@Param("userid") Long userid,
                   @Param("newEmail") String newEmail);

    // 修改密码
    int resetPassword(@Param("userid") Long userid,
                      @Param("newPassword") String newPassword);

    // 更新 VIP
    int updateAccountVIP(Account account);

    // 设置头像
    int setAvatar(@Param("userid") Long userid,
                  @Param("avatar_url") String avatarUrl);

    // 设置登录类型
    int setLoginType(@Param("userid") Long userid,
                     @Param("login_type") String loginType);

    // 更新 token_version（强制让旧 token 失效）
    int updateTokenVersion(@Param("userid") Long userid,
                           @Param("token_version") int tokenVersion);
}
