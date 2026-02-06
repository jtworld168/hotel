# 管理员登录信息 / Administrator Login Credentials

## 快速查看 / Quick Reference

```
用户名 / Username: admin
密码 / Password:   123456
角色 / Role:       管理员 / Administrator
```

---

## 所有演示账号 / All Demo Accounts

| 用户名<br>Username | 密码<br>Password | 角色<br>Role | 说明<br>Description |
|:------------------:|:----------------:|:------------:|:-------------------:|
| **admin**          | **123456**       | 管理员<br>Admin | 系统管理员<br>System Administrator |
| **employee1**      | **123456**       | 员工<br>Employee | 享受20%折扣<br>20% Discount |
| **customer1**      | **123456**       | 顾客<br>Customer | 普通用户<br>Regular User |

---

## 登录步骤 / Login Steps

1. 访问系统首页 / Visit homepage: `http://localhost:3000`
2. 点击右上角"登录"按钮 / Click "Login" button in top-right corner
3. 输入用户名：`admin` / Enter username: `admin`
4. 输入密码：`123456` / Enter password: `123456`
5. 点击"登录"按钮 / Click "Login" button

---

## 注意事项 / Notes

- ⚠️ 这些是演示账号，仅用于测试 / These are demo accounts for testing only
- 🔒 密码在数据库中使用 BCrypt 加密存储 / Passwords are stored with BCrypt encryption
- 📝 所有账号的明文密码都是 `123456` / All accounts use plain text password `123456`
- 🔑 生产环境请修改默认密码 / Change default passwords in production

---

## 相关文档 / Related Documentation

- 📖 [完整 README](README.md) - 项目完整说明文档
- ❓ [中文 FAQ](FAQ_CN.md) - 常见问题解答
- 📊 [实现总结](IMPLEMENTATION_SUMMARY.md) - 技术实现细节

---

## 系统访问地址 / System URLs

| 服务<br>Service | 地址<br>URL | 说明<br>Description |
|:---------------:|:-----------:|:-------------------:|
| 前端<br>Frontend | http://localhost:3000 | 用户界面<br>User Interface |
| 后端<br>Backend | http://localhost:8080 | API 服务<br>API Server |
| API 文档<br>API Docs | http://localhost:8080/api/doc.html | Swagger 文档<br>Swagger Docs |

---

**最后更新 / Last Updated**: 2024-02-05
