package com.catchop.backend.domain.posting.exception

import com.catchop.backend.domain.common.exception.DomainValidationException

/**
 * 유효하지 않은 URL일 때 발생
 * @param url 입력된 URL
 * @param reason 실패 사유
 */
class InvalidUrlException(url: String, reason: String) : DomainValidationException("Invalid url $url : $reason")