package com.catchop.backend.domain.posting.exception

import com.catchop.backend.domain.common.exception.DomainNotFoundException

/** 채용공고를 찾을 수 없을 때 발생 */
class PostingNotFoundException(id: String) : DomainNotFoundException("Posting not found by id $id")