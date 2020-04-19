package com.securenative.models;

public enum ActionType {
    ALLOW {
        @Override
        public String toString() {
            return "allow";
        }
    },
    BLOCK {
        @Override
        public String toString() {
            return "block";
        }
    },
    CHALLENGE {
        @Override
        public String toString() {
            return "challenge";
        }
    },
    BLOCK_IP {
        @Override
        public String toString() {
            return "block_ip";
        }
    },
    BLOCK_USER {
        @Override
        public String toString() {
            return "block_user";
        }
    },
    REDIRECT_IP {
        @Override
        public String toString() {
            return "redirect_ip";
        }
    },
    REDIRECT_USER {
        @Override
        public String toString() {
            return "redirect_user";
        }
    },
    REDIRECT {
        @Override
        public String toString() {
            return "redirect";
        }
    },
    MFA {
        @Override
        public String toString() {
            return "mfa";
        }
    };
}
