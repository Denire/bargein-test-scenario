package com.justai.jaicf.template.scenario

import com.justai.jaicf.builder.Scenario
import com.justai.jaicf.channel.jaicp.telephony


val mainScenario = Scenario(telephony) {

    state("один") {
        activators {
            regex("/start")
            regex("один")
        }
        action {
            reactions.say(
                "Привет! Это какая-нибудь длинная фраза, которую можно перебивать. Бери и перебивай, пока я тут еще буду говорить.",
                bargeIn = true
            )
        }
    }

    /**
     * Проверка ситуации, когда перебиваться должна только одна фраза.
     * */
    state("два") {
        activators {
            regex("два")
            regex("да")
        }
        action {
            // эта фраза не должна перебиваться
            reactions.say(
                "Привет! Это не особо длинная фраза, но ее нет возможности перебить. Она продолжается, меня не перебивает."
            )
            reactions.say(
                "А вот это вторая фраза, ее уже можно перебивать. Вот, попробуй, перебей. А я тут еще поговорю чуток.",
                bargeIn = true
            )
        }
    }

    /**
     * Проверка перебиваня в каком-нибудь оределенном контексте.
     * */
    state("три") {
        activators {
            regex("три")
        }
        action {
            reactions.say(
                "Эту фразу можно перебить словом оператор, и тебе ответит оператор. Еще можно сказать заткнись и отработает вложенный стейт. ",
                bargeInContext = "/ContextHandler"
            )
        }
    }

    /**
     * Проверка смешанных прерываний. Первая фраза не прерывается, вторая - только на стейты в корне (словами один/два/три/четыре), третья фраза ловит только оператор/заткнись/носорог.
     * */
    state("четыре") {
        activators {
            regex("четыре")
        }
        action {
            reactions.say("Вот эту фразу ты не сможешь перебить никак. Она еще продолжается.")
            reactions.say(
                "Вот эту фразу можно перебивать числами от одного до четырех. Я пока поболтаю и подожду, пока ты скажешь какое-нибудь число.",
                bargeIn = true
            )
            reactions.say(
                "Ок, тогда продолжаем, сейчас ты можешь сказать оператор, заткнись, или носорог, чтобы активировать баржин по контексту. Я тут тоже еще поговорю, чтобы ты протестировал.",
                bargeInContext = "/ContextHandler"
            )
        }
    }

    /**
     * Проверка того, как бот восстанавливает контекст, если юзер написал херню и сценарий бросил ошибку в середине баржин-активации.
     * */
    state("пять") {
        activators {
            regex("пять")
        }
        action {
            reactions.say(
                "Ок, перебей меня и смотри в логи. В логах должна быть ошибка, баржина не будет, но бот будет продолжать жить и болтать.",
                bargeInContext = "/SOME_INVALID_STATE_PATH"
            )
        }
    }

    state("ContextHandler") {
        state("оператор") {
            activators {
                regex("оператор")
            }
            action {
                reactions.say("Сейчас позову оператора")
            }
        }
        state("заткнись") {
            activators {
                regex("заткнись")
            }
            action {
                reactions.say("Я заткнулся из вложенного стейта.")
            }
        }
        state("носорог") {
            activators {
                regex("носорог")
            }
            action {
                reactions.say("Да.")
            }
        }
    }

    state("заткнись") {
        activators {
            regex("заткнись")
        }
        action {
            reactions.say("Я заткнулся из топ-левел стейта.")
        }
    }

    fallback {
        reactions.sayRandom("Не понял. Вы сказали: ${request.input}.")
    }
}