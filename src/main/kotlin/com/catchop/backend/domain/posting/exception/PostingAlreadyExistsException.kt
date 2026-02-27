package com.catchop.backend.domain.posting.exception

import com.catchop.backend.domain.common.exception.DomainConflictException

/** 동일 URL의 채용공고가 이미 존재할 때 발생 */
class PostingAlreadyExistsException(url: String) : DomainConflictException("Posting already exists by url $url")