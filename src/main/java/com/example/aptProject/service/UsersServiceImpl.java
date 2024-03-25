package com.example.aptProject.service;

import com.example.aptProject.dao.MyRegionDao;
import com.example.aptProject.dao.UsersDao;
import com.example.aptProject.entity.Users;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {
	@Autowired private UsersDao uDao;
//	@Autowired private MyRegionDao myDao;


	@Override
	public Users getUserByUid(String uid) {
		return uDao.getUser(uid);
	}

	@Override
	public List<Users> getUserList(int page) {
		int offset = (page - 1) * COUNT_PER_PAGE;
		return uDao.getUserList(COUNT_PER_PAGE, offset);
	}

	@Override
	public void registerUser(Users user) {
		String hashedPwd = BCrypt.hashpw(user.getPwd(), BCrypt.gensalt());
		user.setPwd(hashedPwd);
		uDao.insertUser(user);
	}

	@Override
	public void updateUser(Users user) {
		String newPwd = user.getPwd();
		if (newPwd != null && !newPwd.trim().isEmpty()) {
			String hashedPwd = BCrypt.hashpw(newPwd, BCrypt.gensalt());
			user.setPwd(hashedPwd);
		}
		uDao.updateUser(user);
	}

	@Override
	public void deleteUser(String uid) {
		uDao.deleteUser(uid);
	}

	@Override
	public int login(String uid, String pwd) {
		Users user = uDao.getUser(uid);
		if (user == null)
			return USER_NOT_EXIST;
		if (BCrypt.checkpw(pwd, user.getPwd()))
			return CORRECT_LOGIN;
		return WRONG_PASSWORD;
	}


}
