package com.catchop.backend.domain.user.exception

import com.catchop.backend.domain.common.exception.DomainNotFoundException

class UserNotFoundException(id: String) : DomainNotFoundException("User not found by id $id")