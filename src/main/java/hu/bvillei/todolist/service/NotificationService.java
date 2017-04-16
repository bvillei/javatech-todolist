package hu.bvillei.todolist.service;

public interface NotificationService {
	void sendNotification(String to, String subject, String message);
}
