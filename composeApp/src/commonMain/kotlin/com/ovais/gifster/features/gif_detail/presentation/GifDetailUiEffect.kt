package com.ovais.gifster.features.gif_detail.presentation

sealed class GifDetailUiEffect {
    data class ShowMessage(val message: String) : GifDetailUiEffect()
}