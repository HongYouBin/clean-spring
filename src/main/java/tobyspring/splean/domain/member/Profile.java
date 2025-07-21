package tobyspring.splean.domain.member;

import java.util.regex.Pattern;

record Profile(String address) {
    private static final Pattern PROFILE_ADRRESS_PATTERN = Pattern.compile("[a-z0-9]+");

    public Profile {
        if(!PROFILE_ADRRESS_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("프로필 주소 형태가 맞지 않습니다");
        }

        if(address.length() > 15) {
            throw new IllegalArgumentException("프로필 자리는 15자리를 넘으면 안됩니다.");
        }
    }

    public String url() {
        return "@" + address;
    }
}
