package com.csu.userservice.service;

import com.csu.userservice.dto.AccountUpdateRequest;
import com.csu.userservice.entity.Account;
import com.csu.userservice.mapper.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    /** 注册 - 邮箱 */
    public int insertEmailAccount(String email, String username, String password) {
        return accountDao.insertEmailAccount(email, username, password);
    }

    /** GitHub 注册 - 创建账号 */
    public int insertGithubAccount(Account account) {
        return accountDao.insertGithubAccount(account);
    }

    /** GitHub 注册 - 插入 github_account 表 */
    public int insertGithub(Long userid, int github_id, String github_login) {
        return accountDao.insertGithub(userid, github_id, github_login);
    }

    /** GitHub 登录查询 */
    public Account searchGithubAccount(int github_id) {
        return accountDao.searchGithubAccount(github_id);
    }

    /** 普通登录 */
    public Account login(String email, String password) {
        return accountDao.login(email, password);
    }

    /** 通过 userid 查询 */
    public Account getAccountById(Long userid) {
        return accountDao.getAccountById(userid);
    }

    /** 通过 email 查询 */
    public Account getAccountByEmail(String email) {
        return accountDao.getAccountByEmail(email);
    }

    /** 更新用户资料 */
    @Transactional
    public int updateAccount(Long userId, AccountUpdateRequest request) {

        Account account = accountDao.getAccountById(userId);
        if (account == null) {
            return 0;
        }

        account.setUsername(request.getUsername());
        account.setGender(request.getGender());
        account.setAge(request.getAge());
        account.setAddr1(request.getAddr1());
        account.setAddr2(request.getAddr2());
        account.setPhone(request.getPhone());
        account.setAvatarUrl(request.getAvatar_url());

        return accountDao.updateAccount(account);
    }

    /** 修改邮箱 */
    public int resetEmail(Long userid, String newEmail) {
        return accountDao.resetEmail(userid, newEmail);
    }

    /** 修改密码 */
    public int resetPassword(Long userid, String newPassword) {
        return accountDao.resetPassword(userid, newPassword);
    }

    /** 修改 VIP */
    public int updateAccountVIP(Account account) {
        return accountDao.updateAccountVIP(account);
    }

    /** 更新头像 */
    public int setAvatar(Long userid, String avatar_url) {
        return accountDao.setAvatar(userid, avatar_url);
    }

    /** 修改登录方式（邮箱登录 / GitHub 登录） */
    public int setLoginType(Long userid, String login_type) {
        return accountDao.setLoginType(userid, login_type);
    }

    /** Token 版本号 +1（用于强制下线） */
    public int updateTokenVersion(Long userid) {
        Account account = accountDao.getAccountById(userid);
        if (account == null) return 0;

        return accountDao.updateTokenVersion(userid, account.getTokenVersion() + 1);
    }
}
