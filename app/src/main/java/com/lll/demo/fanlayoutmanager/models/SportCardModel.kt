package com.lll.demo.fanlayoutmanager.models

import android.os.Parcelable
import java.io.Serializable

class SportCardModel(builder: Builder) : Serializable {

    var sportTitle: String? = null
    var sportSubtitle: String? = null
    var sportRound: String? = null

    var imageResId: Int = 0

    var time: String? = null
    var dayPart: String? = null

    var backgroundColorResId: Int = 0

    init {
        sportTitle = builder.sportTitle
        sportSubtitle = builder.sportSubtitle
        sportRound = builder.sportRound
        imageResId = builder.imageResId
        time = builder.time
        dayPart = builder.dayPart
        backgroundColorResId = builder.backgroundColorResId
    }


    companion object {
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    class Builder {
        var sportTitle: String? = null
        var sportSubtitle: String? = null
        var sportRound: String? = null
        var imageResId: Int = 0
        var time: String? = null
        var dayPart: String? = null
        var backgroundColorResId: Int = 0

        /**
         * Sets the `sportTitle` and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param sportTitle the `sportTitle` to set
         * @return a reference to this Builder
         */
        fun withSportTitle(sportTitle: String): Builder {
            this.sportTitle = sportTitle
            return this
        }

        /**
         * Sets the `sportSubtitle` and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param sportSubtitle the `sportSubtitle` to set
         * @return a reference to this Builder
         */
        fun withSportSubtitle(sportSubtitle: String): Builder {
            this.sportSubtitle = sportSubtitle
            return this
        }

        /**
         * Sets the `sportRound` and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param sportRound the `sportRound` to set
         * @return a reference to this Builder
         */
        fun withSportRound(sportRound: String): Builder {
            this.sportRound = sportRound
            return this
        }

        /**
         * Sets the `imageResId` and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param imageResId the `imageResId` to set
         * @return a reference to this Builder
         */
        fun withImageResId(imageResId: Int): Builder {
            this.imageResId = imageResId
            return this
        }

        /**
         * Sets the `time` and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param time the `time` to set
         * @return a reference to this Builder
         */
        fun withTime(time: String): Builder {
            this.time = time
            return this
        }

        /**
         * Sets the `dayPart` and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param dayPart the `dayPart` to set
         * @return a reference to this Builder
         */
        fun withDayPart(dayPart: String): Builder {
            this.dayPart = dayPart
            return this
        }

        /**
         * Sets the `backgroundColorResId` and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param backgroundColorResId the `backgroundColorResId` to set
         * @return a reference to this Builder
         */
        fun withBackgroundColorResId(backgroundColorResId: Int): Builder {
            this.backgroundColorResId = backgroundColorResId
            return this
        }

        /**
         * Returns a `SportCardModel` built from the parameters previously set.
         *
         * @return a `SportCardModel` built with parameters of this `SportCardModel.Builder`
         */
        fun build(): SportCardModel {
            return SportCardModel(this)
        }
    }
}