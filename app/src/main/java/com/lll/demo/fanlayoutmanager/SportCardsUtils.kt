package com.lll.demo.fanlayoutmanager

import com.lll.demo.fanlayoutmanager.models.SportCardModel
import com.lll.layoutmanager.demo.R
import java.util.ArrayList

object SportCardsUtils {

    fun generateSportCards(): List<SportCardModel> {
        val sportCardModels = ArrayList<SportCardModel>(5)

        run {
            sportCardModels.add(SportCardModel
                    .newBuilder()
                    .withSportTitle("Table tennis")
                    .withSportSubtitle("Woman's singles")
                    .withSportRound("Preliminaries")
                    .withImageResId(R.drawable.pic_card_1)
                    .withTime("3:00")
                    .withDayPart("PM")
                    .withBackgroundColorResId(R.color.dark_orchid)
                    .build())

        }

        run {
            sportCardModels.add(SportCardModel
                    .newBuilder()
                    .withSportTitle("Shooting")
                    .withSportSubtitle("Woman's 10m air rifle")
                    .withSportRound("Qualification")
                    .withImageResId(R.drawable.pic_card_2)
                    .withTime("2:30")
                    .withDayPart("PM")
                    .withBackgroundColorResId(R.color.mantis)
                    .build())

        }

        run {
            sportCardModels.add(SportCardModel
                    .newBuilder()
                    .withSportTitle("Rowing")
                    .withSportSubtitle("Men's single sculls")
                    .withSportRound("Heats")
                    .withImageResId(R.drawable.pic_card_3)
                    .withTime("2:30")
                    .withDayPart("PM")
                    .withBackgroundColorResId(R.color.usc_gold)
                    .build())

        }

        run {
            sportCardModels.add(SportCardModel
                    .newBuilder()
                    .withSportTitle("Archery")
                    .withSportSubtitle("Men's team")
                    .withSportRound("Round of 16")
                    .withImageResId(R.drawable.pic_card_5)
                    .withTime("3:00")
                    .withDayPart("PM")
                    .withBackgroundColorResId(R.color.portland_orange)
                    .build())

        }

        run {
            sportCardModels.add(SportCardModel
                    .newBuilder()
                    .withSportTitle("Water polo")
                    .withSportSubtitle("Men’s tournament")
                    .withSportRound("Group A")
                    .withImageResId(R.drawable.pic_card_4)
                    .withTime("3:00")
                    .withDayPart("PM")
                    .withBackgroundColorResId(R.color.dodger_blue)
                    .build())

        }
        return sportCardModels
    }
}