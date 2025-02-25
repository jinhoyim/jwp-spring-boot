package myblog.user.dto;

public class SessionedUser {
    public static final String SESSIONED_USER_KEY = "sessionedUser";

    public static final SessionedUser GUEST = new GuestUser();

    private long id;

    private String userId;

    private SessionedUser() {
    }

    public SessionedUser(long id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public boolean isOwner(long id) {
        return this.id == id;
    }

    public long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isGuest() {
        return false;
    }

    @Override
    public String toString() {
        return "SessionedUser{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                '}';
    }

    private static class GuestUser extends SessionedUser {
        @Override
        public boolean isGuest() {
            return true;
        }

        @Override
        public String toString() {
            return "GuestUser";
        }
    }
}
