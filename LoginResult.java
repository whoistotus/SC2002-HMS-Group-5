public class LoginResult {
    private boolean success;
    private String message;
    private User user;
    private boolean firstLogin;

    public LoginResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResult(boolean success, String message, User user, boolean firstLogin) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.firstLogin = firstLogin;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }
}
