package com.formaloo.loyalty.common


class ViewFailure {
    class responseError(msg: String?) : Failure.FeatureFailure(msg)
}
