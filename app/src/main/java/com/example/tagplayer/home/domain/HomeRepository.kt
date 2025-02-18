package com.example.tagplayer.home.domain

import com.example.tagplayer.core.domain.PlayForeground
import com.example.tagplayer.core.domain.ScanForeground

interface HomeRepository<T> : HomeRecently<T>, HomeLibrary<T>, PlayForeground, ScanForeground

