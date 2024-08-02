package com.nus.iss.funsg;

import java.util.List;

public class AuthEventsResponse {
    private Long id;
    private String name;
    private String description;
    private String start;
    private String end;
    private String location;
    private Long groupId;
    private String groupName;
    private String createdAt;
    private User createdBy;
    private String profileImagePath;
    private int maxParticipants;
    private List<User> eventParticipants;

    public AuthEventsResponse(Long id, String name, String description, String start, String end, String location, Long groupId, String groupName, String createdAt, User createdBy, String profileImagePath, int maxParticipants, List<User> eventParticipants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.location = location;
        this.groupId = groupId;
        this.groupName = groupName;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.profileImagePath = profileImagePath;
        this.maxParticipants = maxParticipants;
        this.eventParticipants = eventParticipants;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public List<User> getEventParticipants() {
        return eventParticipants;
    }

    public void setEventParticipants(List<User> eventParticipants) {
        this.eventParticipants = eventParticipants;
    }

    public static class User{
        private long userId;
        private String name;
        private String email;
        private String profileImage;
        private String createdAt;

        public User(long userId, String name, String email, String profileImage, String createdAt) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.profileImage = profileImage;
            this.createdAt = createdAt;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }


}
