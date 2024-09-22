package com.example.tagplayer.filter_by_tags

import com.example.tagplayer.core.CustomObservable

class FilterTagObservable : CustomObservable.ManualClear<TagsFilterState>(TagsFilterState.Empty)