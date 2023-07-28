package io.wisoft.capstonedesign.domain.auth.web;

import io.wisoft.capstonedesign.domain.auth.web.dto.TokenResponse;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import io.wisoft.capstonedesign.global.exception.token.InvalidTokenException;
import io.wisoft.capstonedesign.global.jwt.AuthorizationExtractor;
import io.wisoft.capstonedesign.global.jwt.JwtTokenProvider;
import io.wisoft.capstonedesign.global.redis.RedisAdapter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
public class JwtReIssueController {

    @Value("${security.jwt.token.token-type}")
    private String tokenType;

    private final RedisAdapter redisAdapter;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor extractor;
    private final MemberRepository memberRepository;

    @GetMapping("/jwt/re-issuance")
    public ResponseEntity<TokenResponse> reIssueAccessToken(final HttpServletRequest request) {

        final String refreshToken = extractor.extract(request, tokenType);
        final String email = jwtTokenProvider.getSubject(refreshToken);

        final Long id = extractId(email);

        if (!redisAdapter.hasKey(email)) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다", ErrorCode.INVALID_TOKEN);
        }

        final String reIssuedAccessToken = jwtTokenProvider.createAccessToken(email);

        log.info("{}님에게 accessToken {}을 재발급합니다.", email, reIssuedAccessToken);
        return ResponseEntity.ok(new TokenResponse(id, tokenType, reIssuedAccessToken, refreshToken));
    }

    private Long extractId(final String email) {

        final Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            return member.get().getId();
        }

        throw new NotFoundException("이메일이 유효하지 않습니다.");
    }
}
