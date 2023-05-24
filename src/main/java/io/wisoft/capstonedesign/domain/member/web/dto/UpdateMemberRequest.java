package io.wisoft.capstonedesign.domain.member.web.dto;

public record UpdateMemberRequest(
        String photoPath,
        String nickname) { }