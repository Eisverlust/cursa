package com.example.service

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.logging.LogLevel
import com.github.kotlintelegrambot.network.fold

object BotService {

    private val idAppView = Regex("№[ 0-9]{1,8} ")
    private val getID = Regex("[0-9]{1,8}")

    private val empService = EmpService()
    private val appViewService = AppView()
    private val users = empService.allEmpLoad().apply {
        println(this)
    }
    private val bot = bot {
        logLevel = LogLevel.Error
        token = System.getenv("token")//ха ха теперь тут нет моего токена обломитесь :D
        dispatch {
            appViewFun()
            regEmp()
            acceptedApp()
            help()
            commentary()

        }
    }

    fun Dispatcher.regEmp() {
        command("registration") {
            if (!isReg(message.chat.id.toString())) {
                if (args.size in 3..5) {
                    val phone = args[0]
                    val fio = "${args[1]} ${args[2]}"
                    val group = args[3]
                    empService.addEmp(phone, group, fio, message.chat.id.toString())
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Успешно!")
                    users.add(message.chat.id.toString())
                } else {
                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        text = "Введите значения в формате /registration 8902552**** Пупа Пупкин Ис-2077 в поле вместе с командой!"
                    )
                }
            } else
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "Ты зачем клонировать себя решил ?"
                )
        }
    }

    fun start() {
        bot.startPolling()

    }

    private fun isReg(chatID: String) = if (users.contains(chatID))
        true
    else empService.findTelegramID(chatID) != null


    fun stop() {
        bot.stopPolling()
    }

    fun Dispatcher.appViewFun() {

        val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
            listOf(InlineKeyboardButton.CallbackData(text = "Принять", callbackData = "testButton")),
            /*listOf(InlineKeyboardButton.CallbackData(text = "Жалоба(to-do)", callbackData = "showAlert"))*/
        )
        command("ShowApp") {
            if (isReg(message.chat.id.toString())) {
                if (!appViewService.isApp(chatId = message.chat.id)) {
                    bot.sendMessage(ChatId.fromId(message.chat.id), text = "Вот все заявки")
                    appViewService.showAllAppDONTMAGAZINA().forEach {
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = it,
                            replyMarkup = inlineKeyboardMarkup
                        ).fold({}, {
                            bot.sendMessage(ChatId.fromId(message.chat.id), "У вас еще не закрыта вакансия")
                        })
                        //bot.deleteMessage(ChatId.fromId(message.chat.id), callbackQuery.message!!.messageId)

                    }
                    callbackQuery("testButton") {

                        val result = idAppView.find(callbackQuery.message?.text!!)?.value
                        val finalResultID = getID.find(result!!)?.value!!.toInt()
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        appViewService.acceptApp(finalResultID, chatId.toString())
                        /*            bot.sendMessage(ChatId.fromId(chatId), text = callbackQuery.message!!.messageId.toString())*/
                        val inlineKeyboardMarkup2 = InlineKeyboardMarkup.create(
                            listOf(
                                InlineKeyboardButton.CallbackData(text = "Завершить", callbackData = "complete"),
                                InlineKeyboardButton.CallbackData(text = "Отменить", callbackData = "error")
                            )
                        )
                        val meess = appViewService.showAcceptedApp(chatId = message.chat.id)
                        bot.sendMessage(
                            ChatId.fromId(message.chat.id), text = meess.toString(),
                            replyMarkup = inlineKeyboardMarkup2
                        )
                        /* bot.sendMessage(ChatId.fromId(chatId), text = "${callbackQuery.message!!.text}")*/
                        bot.deleteMessage(ChatId.fromId(chatId), callbackQuery.message!!.messageId)

                    }
                    /*callbackQuery("showAlert") {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        bot.sendMessage(
                            ChatId.fromId(chatId),
                            text = "${callbackQuery.message!!.text} ?"
                        )// жопа была красивой и делала многое
                    }*/
                } else
                    bot.sendMessage(ChatId.fromId(message.chat.id), "У вас уже есть заявка")
            } else
                notAuth(message.chat.id)
        }
    }

    fun Dispatcher.acceptedApp() {
        val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
            listOf(
                InlineKeyboardButton.CallbackData(text = "Завершить", callbackData = "complete"),
                InlineKeyboardButton.CallbackData(text = "Отменить", callbackData = "error")
            )
        )
        command("acceptedApp") {
            if (isReg(message.chat.id.toString())) {
                val meess = appViewService.showAcceptedApp(chatId = message.chat.id)
                if (meess.first != null)
                bot.sendMessage(
                    ChatId.fromId(message.chat.id), text = meess.toString(),
                    replyMarkup = inlineKeyboardMarkup
                )
                else
                    bot.sendMessage( ChatId.fromId(message.chat.id),
                        text = "Активных заявок нет")
            }
            else
                notAuth(message.chat.id)
        }
        callbackQuery("complete") {
            val result = idAppView.find(callbackQuery.message?.text!!)?.value
            val finalResultID = getID.find(result!!)?.value!!.toInt()
            appViewService.completeApp(ID = finalResultID)
            bot.deleteMessage(ChatId.fromId(callbackQuery.message!!.chat.id), callbackQuery.message!!.messageId)
            bot.sendMessage(ChatId.fromId(callbackQuery.message!!.chat.id), text = "Статус изменен")
        }
        callbackQuery("error") {
            val result = idAppView.find(callbackQuery.message?.text!!)?.value
            val finalResultID = getID.find(result!!)?.value!!.toInt()
            appViewService.cancel(ID = finalResultID)
            bot.deleteMessage(ChatId.fromId(callbackQuery.message!!.chat.id), callbackQuery.message!!.messageId)
            bot.sendMessage(ChatId.fromId(callbackQuery.message!!.chat.id), text = "Заявка оменена")
        }
    }


    fun notAuth(chatID: Long) {
        bot.sendMessage(
            chatId = ChatId.fromId(chatID),
            text = "Э ты хто?"
        )
    }

    fun Dispatcher.help() {
        command("help") {
            bot.sendMessage(
                ChatId.fromId(message.chat.id), text = "/registration - зарегистрироватся" + "\n" +
                        "/ShowApp - Показать заявки" + "\n" +
                        "/acceptedApp - присвоеная заявка" + "\n" +
                        "/c - Оставить коментарий последней принятой заявке"
            )
        }
    }

    fun Dispatcher.commentary() {
        command("c") {
            if (isReg(message.chat.id.toString())) {
                if (args.isNotEmpty()) {
                    val comm2 = args.joinToString()
                    appViewService.commentary(message.chat.id, comm2)
                    bot.sendMessage(ChatId.fromId(message.chat.id), text = "Коментарий оставлен")
                } else {
                    bot.sendMessage(ChatId.fromId(message.chat.id), text = "Коментарий не может быть пустым")
                }
            }
            else
                notAuth(message.chat.id)
        }
    }
}