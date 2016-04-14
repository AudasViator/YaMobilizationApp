package pro.audasviator.yamobilizationapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ArtistFetcher {
    private static final String JSON_URL = "http://download.cdn.yandex.net/mobilization-2016/artists.json";

    public String getJson() throws IOException {

        // TODO: Don't forget to remove test json

        String json = "[\n" +
                "\t{\n" +
                "\t\t\"id\": 1080505,\n" +
                "\t\t\"name\": \"Tove Lo\",\n" +
                "\t\t\"genres\": [ \"pop\", \"dance\", \"electronics\" ],\n" +
                "\t\t\"tracks\": 81,\n" +
                "\t\t\"albums\": 22,\n" +
                "\t\t\"link\": \"http://www.tove-lo.com/\",\n" +
                "\t\t\"description\": \"шведская певица и автор песен. Она привлекла к себе внимание в 2013 году с выпуском сингла «Habits», но настоящего успеха добилась с ремиксом хип-хоп продюсера Hippie Sabotage на эту песню, который получил название «Stay High». 4 марта 2014 года вышел её дебютный мини-альбом Truth Serum, а 24 сентября этого же года дебютный студийный альбом Queen of the Clouds. Туве Лу является автором песен таких артистов, как Icona Pop, Girls Aloud и Шер Ллойд.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 2915,\n" +
                "\t\t\"name\": \"Ne-Yo\",\n" +
                "\t\t\"genres\": [ \"rnb\", \"pop\", \"rap\" ],\n" +
                "\t\t\"tracks\": 256,\n" +
                "\t\t\"albums\": 152,\n" +
                "\t\t\"link\": \"http://www.neyothegentleman.com/\",\n" +
                "\t\t\"description\": \"обладатель трёх премии Грэмми, американский певец, автор песен, продюсер, актёр, филантроп. В 2009 году журнал Billboard поставил Ни-Йо на 57 место в рейтинге «Артисты десятилетия».\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/15ae00fc.p.2915/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/15ae00fc.p.2915/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 91546,\n" +
                "\t\t\"name\": \"Usher\",\n" +
                "\t\t\"genres\": [ \"rnb\", \"pop\", \"rap\" ],\n" +
                "\t\t\"tracks\": 450,\n" +
                "\t\t\"albums\": 183,\n" +
                "\t\t\"link\": \"http://usherworld.com/\",\n" +
                "\t\t\"description\": \"американский певец и актёр. Один из самых коммерчески успешных R&B-музыкантов афроамериканского происхождения. В настоящее время продано более 65 миллионов копий его альбомов по всему миру. Выиграл семь премий «Грэмми», четыре премии World Music Awards, четыре премии American Music Award и девятнадцать премий Billboard Music Awards. Владелец собственной звукозаписывающей компании US Records. Он занимает 21 место в списке самых успешных музыкантов по версии Billboard, а также второе место, уступив Эминему в списке самых успешных музыкантов 2000-х годов. В 2010 году журнал Glamour включил его в список 50 самых сексуальных мужчин.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/b0e14f75.p.91546/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/b0e14f75.p.91546/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 100500,\n" +
                "\t\t\"name\": \"Jay Sean\",\n" +
                "\t\t\"genres\": [ \"pop\", \"rap\", \"rnb\" ],\n" +
                "\t\t\"tracks\": 106,\n" +
                "\t\t\"albums\": 38,\n" +
                "\t\t\"description\": \"британский рэпер, являющийся выходцем из Индии. Родился в западном Лондоне, Англия. Выпустил три альбома Me Against Myself, My Own Way и All or Nothing.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/db35e57a.p.100500/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/db35e57a.p.100500/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 74614,\n" +
                "\t\t\"name\": \"Kelly Rowland\",\n" +
                "\t\t\"genres\": [ \"rnb\", \"pop\", \"rap\" ],\n" +
                "\t\t\"tracks\": 174,\n" +
                "\t\t\"albums\": 106,\n" +
                "\t\t\"link\": \"http://www.kellyrowland.com/\",\n" +
                "\t\t\"description\": \"американская певица и актриса. Выступает в стиле современный ритм-энд-блюз, является автором текстов песен.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/be7f0f49.p.74614/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/be7f0f49.p.74614/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 1156,\n" +
                "\t\t\"name\": \"Timbaland\",\n" +
                "\t\t\"genres\": [ \"rap\", \"pop\", \"dance\" ],\n" +
                "\t\t\"tracks\": 227,\n" +
                "\t\t\"albums\": 141,\n" +
                "\t\t\"link\": \"http://www.timbalandmusic.com/\",\n" +
                "\t\t\"description\": \"американский рэпер, музыкальный продюсер, аранжировщик и автор песен. Основатель лейбла Mosley Music Group, двукратный обладатель премий «Грэмми».\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/e33024d5.p.1156/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/e33024d5.p.1156/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 1150,\n" +
                "\t\t\"name\": \"Keri Hilson\",\n" +
                "\t\t\"genres\": [ \"pop\", \"rnb\", \"rap\" ],\n" +
                "\t\t\"tracks\": 99,\n" +
                "\t\t\"albums\": 64,\n" +
                "\t\t\"description\": \"американская певица и автор песен в стиле современного R&B, работает на лейблы Zone 4/Mosley Music Group/Interscope Records. Является членом объединения авторов и продюсеров, известного как The Clutch.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/40598113.p.1150/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/40598113.p.1150/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 451523,\n" +
                "\t\t\"name\": \"Пицца\",\n" +
                "\t\t\"genres\": [ \"rusrap\" ],\n" +
                "\t\t\"tracks\": 33,\n" +
                "\t\t\"albums\": 11,\n" +
                "\t\t\"link\": \"http://pizzamusic.ru/\",\n" +
                "\t\t\"description\": \"музыкальная группа, основанная в 2009 году Сергеем Приказчиковым и исполняющая песни на стыке таких жанров, как поп-соул, регги, фанк и даже рэп.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/120513b9.p.451523/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/120513b9.p.451523/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 41110,\n" +
                "\t\t\"name\": \"Дима Билан\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 119,\n" +
                "\t\t\"albums\": 29,\n" +
                "\t\t\"link\": \"http://bilandima.ru/\",\n" +
                "\t\t\"description\": \"Дима Николаевич Билан - российский певец, более известный как Дима Билан. В июне 2008 года принял данный псевдоним в качестве настоящего имени и фамилии. Дима Билан представлял Россию на конкурсе песни «Евровидение» два раза: в 2006 году с песней «Never Let You Go», заняв второе место, и в 2008 году с песней «Believe», заняв первое место и став первым российским артистом, победившим на конкурсе песни «Евровидение».\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/20c848e3.p.41110/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/20c848e3.p.41110/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 166300,\n" +
                "\t\t\"name\": \"Бьянка\",\n" +
                "\t\t\"genres\": [ \"pop\", \"rnb\" ],\n" +
                "\t\t\"tracks\": 45,\n" +
                "\t\t\"albums\": 13,\n" +
                "\t\t\"link\": \"http://www.biankanumber1.ru/\",\n" +
                "\t\t\"description\": \"белорусская и российская R&B-исполнительница.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/f960f99a.p.166300/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/f960f99a.p.166300/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 161010,\n" +
                "\t\t\"name\": \"Нюша\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 99,\n" +
                "\t\t\"albums\": 34,\n" +
                "\t\t\"link\": \"http://www.nyusha.ru/\",\n" +
                "\t\t\"description\": \"российская певица, автор песен, композитор, актриса.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/7806607c.p.161010/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/7806607c.p.161010/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 155914,\n" +
                "\t\t\"name\": \"Винтаж\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 91,\n" +
                "\t\t\"albums\": 17,\n" +
                "\t\t\"link\": \"http://www.vintagemusic.ru/\",\n" +
                "\t\t\"description\": \"российская поп-группа, в состав которой входят певица Анна Плетнёва и певец, композитор и саунд-продюсер Алексей Романов. Ранее в состав группы входили танцовщицы Мия и Светлана Иванова.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/42479f15.p.155914/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/42479f15.p.155914/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 159009,\n" +
                "\t\t\"name\": \"Градусы\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 30,\n" +
                "\t\t\"albums\": 5,\n" +
                "\t\t\"link\": \"http://www.gradusy.com/\",\n" +
                "\t\t\"description\": \"российская поп-группа из Ставрополя, основанная в 2008 году. Первый концерт группа отыграла под названием «Градус 100» 29 мая 2008 года\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/46f09c63.p.159009/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/46f09c63.p.159009/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 755171,\n" +
                "\t\t\"name\": \"Иван Дорн\",\n" +
                "\t\t\"genres\": [ \"pop\", \"rap\", \"electronics\" ],\n" +
                "\t\t\"tracks\": 117,\n" +
                "\t\t\"albums\": 43,\n" +
                "\t\t\"link\": \"http://www.ivandorn.com\",\n" +
                "\t\t\"description\": \"украинский певец, диджей и телеведущий, бывший участник группы «Пара Нормальных».\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/f8bb3e2e.p.755171/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/f8bb3e2e.p.755171/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 2392189,\n" +
                "\t\t\"name\": \"Андрей Леницкий\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 29,\n" +
                "\t\t\"albums\": 4,\n" +
                "\t\t\"link\": \"http://lenitsky.com/\",\n" +
                "\t\t\"description\": \"Украинский музыкант, солист, автор песен и композитор.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/db0fe697.p.2392189/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/db0fe697.p.2392189/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 796797,\n" +
                "\t\t\"name\": \"Егор Крид\",\n" +
                "\t\t\"genres\": [ \"pop\", \"rnb\", \"rap\" ],\n" +
                "\t\t\"tracks\": 57,\n" +
                "\t\t\"albums\": 22,\n" +
                "\t\t\"link\": \"https://twitter.com/egorkreed\",\n" +
                "\t\t\"description\": \"российский певец.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/eed9efaa.p.796797/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/eed9efaa.p.796797/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 190602,\n" +
                "\t\t\"name\": \"5sta family\",\n" +
                "\t\t\"genres\": [ \"pop\", \"rap\", \"soul\" ],\n" +
                "\t\t\"tracks\": 34,\n" +
                "\t\t\"albums\": 13,\n" +
                "\t\t\"link\": \"http://5sta.ru\",\n" +
                "\t\t\"description\": \"российская R&B-группа.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/c3713853.p.190602/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/c3713853.p.190602/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 6173,\n" +
                "\t\t\"name\": \"Sugababes\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 143,\n" +
                "\t\t\"albums\": 55,\n" +
                "\t\t\"link\": \"https://twitter.com/SugababesHQ\",\n" +
                "\t\t\"description\": \"женское поп-трио из Лондона, которое было сформировано в 1998-м году. Эта группа выпустила 27 синглов, шесть из которых стали № 1 в Великобритании, и семь альбомов, два из которых также забрались на вершину Британского альбомного чарта. Три альбома девушек стали трижды платиновыми. В 2003-м году они победили в номинации «Лучший танцевальный исполнитель», а в 2006-м году были названы в Великобритании исполнительницами двадцать первого века, опережая таких артистов, как Бритни Спирс и Мадонна. По всему миру было куплено более четырнадцати миллионов копий альбомов Sugababes.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/3a78c5ba.a.2987-1/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/3a78c5ba.a.2987-1/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 29506,\n" +
                "\t\t\"name\": \"Shaznay Lewis\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 17,\n" +
                "\t\t\"albums\": 3,\n" +
                "\t\t\"description\": \"британская певица, автор песен и актриса.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/fb79d2d3.a.15587-2/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/fb79d2d3.a.15587-2/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 11580,\n" +
                "\t\t\"name\": \"Emma Bunton\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 20,\n" +
                "\t\t\"albums\": 11,\n" +
                "\t\t\"description\": \"британская певица, автор песен и телеведущая.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/dfddb106.a.32140-1/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/dfddb106.a.32140-1/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 36748,\n" +
                "\t\t\"name\": \"Geri Halliwell\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 56,\n" +
                "\t\t\"albums\": 20,\n" +
                "\t\t\"description\": \"британская певица, автор песен и детская писательница. Добилась известности в середине 1990-х годов как участница женской поп-группы Spice Girls. Холлиуэлл сделала самую успешную сольную карьеру из всех участниц группы, была четырежды номинирована на Brit Awards, выпустила четыре сингла, достигших первого места в британских хит-парадах, а суммарные продажи её альбомов превышают четыре миллиона копий.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/56e6e674.a.31311-1/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/56e6e674.a.31311-1/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 6190,\n" +
                "\t\t\"name\": \"Mutya Buena\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 5,\n" +
                "\t\t\"albums\": 4,\n" +
                "\t\t\"description\": \"британская певица, бывшая участница группы «Sugababes».\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/c721d84d.a.88046-1/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/c721d84d.a.88046-1/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 36976,\n" +
                "\t\t\"name\": \"Spice Girls\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 93,\n" +
                "\t\t\"albums\": 28,\n" +
                "\t\t\"link\": \"http://thespicegirls.com/\",\n" +
                "\t\t\"description\": \"британская женская поп-группа, образованная в Лондоне в 1994 году.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/e3bdb6ba.a.35579-1/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/e3bdb6ba.a.35579-1/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 6875,\n" +
                "\t\t\"name\": \"Girls Aloud\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 71,\n" +
                "\t\t\"albums\": 31,\n" +
                "\t\t\"description\": \"британская поп-группа, сформировавшаяся в 2002 году в ходе реалити-шоу канала ITV Popstars: The Rivals. В состав входят Шерил Коул, Сара Хардинг, Никола Робертс, Кимберли Уолш и Надин Койл. Группа выпустила двадцать два сингла, пять студийных альбомов, два сборника лучших хитов и два альбома ремиксов. Все альбомы Girls Aloud также были сертифицированы как платиновые в Великобритании. Группа была пятикратно номинирована на BRIT Awards и в 2009 году одержала победу в номинации «Лучший сингл» за песню «The Promise».\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/b41fab5f.p.6875/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/b41fab5f.p.6875/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 107361,\n" +
                "\t\t\"name\": \"Melanie C\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 60,\n" +
                "\t\t\"albums\": 24,\n" +
                "\t\t\"link\": \"http://www.melaniec.net/\",\n" +
                "\t\t\"description\": \"британская певица, автор песен. Наиболее известна как участница самой популярной в истории развития музыкального бизнеса девичьей группы «Spice Girls», где выступала под псевдонимом «Sporty Spice». Она также известна под псевдонимами «Mel C» или «Melanie C».\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/cacf3e7c.a.35684-1/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/cacf3e7c.a.35684-1/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 10205,\n" +
                "\t\t\"name\": \"Tokio Hotel\",\n" +
                "\t\t\"genres\": [ \"alternative\" ],\n" +
                "\t\t\"tracks\": 150,\n" +
                "\t\t\"albums\": 23,\n" +
                "\t\t\"link\": \"http://www.tokiohotel.com/\",\n" +
                "\t\t\"description\": \"немецкая синтипоп группа, образованная в 2001 году в Магдебурге. В составе: Билл Каулитц, Том Каулитц, Густав Шефер, Георг Листинг.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/0684769b.p.10205/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/0684769b.p.10205/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 89913,\n" +
                "\t\t\"name\": \"Hilary Duff\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 22,\n" +
                "\t\t\"albums\": 10,\n" +
                "\t\t\"link\": \"http://hilaryduff.com/\",\n" +
                "\t\t\"description\": \"американская актриса, певица, предприниматель, модель и продюсер. Родилась и выросла в Хьюстоне, Техас. Появилась в нескольких местных театральных постановках и ТВ-рекламах в раннем детстве. Дафф стала кумиром подростков после того, как снялась в сериале канала Disney Channel «Лиззи Магуайер» в начале 2000-х и фильме «Лиззи Магуайер», в котором она появилась в одноимённой роли. Позже она снялась ещё в нескольких фильмах «Агент Коди Бэнкс», «Оптом дешевле», «История Золушки» и «Оптом дешевле 2», которые были самыми успешными в её карьере...\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/024331a0.p.89913/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/024331a0.p.89913/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 677449,\n" +
                "\t\t\"name\": \"Carly Rae Jepsen\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 68,\n" +
                "\t\t\"albums\": 19,\n" +
                "\t\t\"link\": \"http://www.carlyraemusic.com/\",\n" +
                "\t\t\"description\": \"канадская певица из Британской Колумбии. В 2007 году она участвовала в пятом сезоне Canadian Idol. Вскоре после участия на Canadian Idol она подписала контракт с рекорд-лейблами Fontana и MapleMusic и 30 сентября 2008 года выпустила свой дебютный альбом Tug of War. Три года спустя она выпустила новый сингл под названием «Call Me Maybe», за ним последовал новый альбом Curiosity, вышедший 14 февраля 2012 года. «Call Me Maybe» на сегодняшний день самый успешный сингл певицы, он достиг 1-го места в Billboard Hot 100 и вершины Canadian Hot 100. Джепсен подписала контракт с Interscope. 14 апреля 2012 песня «Call Me Maybe» возглавила UK Singles Chart.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/7d17d6fd.p.677449/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/7d17d6fd.p.677449/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 57574,\n" +
                "\t\t\"name\": \"Bonnie McKee\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 27,\n" +
                "\t\t\"albums\": 14,\n" +
                "\t\t\"description\": \"американская певица, автор песен, актриса. В 2011 году названа журналом Rolling Stone «Лучшим Секретным Оружием». МакКи является соавтором восьми синглов номер один, которые были проданы в размере 25 млн копий по всему миру. Она работает с поп-звездой Кэти Перри и в соавторстве написала такие хиты как «California Gurls», «Teenage Dream», «Last Friday Night », «Part of Me», «Wide Awake», «Roar». Также Бонни является соавтором песни Тайо Круза «Dynamite», которая стала второй самой продаваемой песней британского артиста в цифровую эпоху...\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/48d84037.a.14352-1/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/48d84037.a.14352-1/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 204423,\n" +
                "\t\t\"name\": \"Allison Iraheta\",\n" +
                "\t\t\"genres\": [ \"pop\" ],\n" +
                "\t\t\"tracks\": 21,\n" +
                "\t\t\"albums\": 4,\n" +
                "\t\t\"description\": \"американская певица, победительница музыкального конкурса «Quinceañera: Mama Quiero Ser Artista», финалистка American Idol сезона 2009 года. Выпустила в 2009 году свой первый альбом Just Like You. Живёт в Лос-Анджелесе.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/47c3970c.a.74886-1/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/47c3970c.a.74886-1/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 98403,\n" +
                "\t\t\"name\": \"Kelly Clarkson\",\n" +
                "\t\t\"genres\": [ \"pop\", \"dance\", \"country\" ],\n" +
                "\t\t\"tracks\": 242,\n" +
                "\t\t\"albums\": 92,\n" +
                "\t\t\"link\": \"http://www.kellyclarkson.com/\",\n" +
                "\t\t\"description\": \"американская певица и актриса. Кларксон обрела известность после участия в телешоу American Idol в 2002 году и представляла США в конкурсе «World Idol» в 2003-м. В США было продано более 10,5 миллионов копий её альбомов.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/f47d4860.p.98403/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/f47d4860.p.98403/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 4632,\n" +
                "\t\t\"name\": \"Ciara\",\n" +
                "\t\t\"genres\": [ \"pop\", \"rnb\", \"rap\" ],\n" +
                "\t\t\"tracks\": 156,\n" +
                "\t\t\"albums\": 84,\n" +
                "\t\t\"link\": \"http://onlyciara.com/\",\n" +
                "\t\t\"description\": \"американская певица, автор песен, продюсер, танцовщица, актриса, модель, режиссёр клипов. Сиара дебютировала летом 2004 с синглом «Goodies», попавшим наверх чарта Billboard Hot 100. Одноимённый альбом был выпущен в США 28 сентября 2004, его продажи составили более пяти млн в мире и получил множество наград и номинаций.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/fc971dc6.p.4632/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/fc971dc6.p.4632/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 1636897,\n" +
                "\t\t\"name\": \"Мот\",\n" +
                "\t\t\"genres\": [ \"rusrap\" ],\n" +
                "\t\t\"tracks\": 60,\n" +
                "\t\t\"albums\": 27,\n" +
                "\t\t\"link\": \"http://www.black-star.ru/\",\n" +
                "\t\t\"description\": \"рэп-исполнитель, бывший участник музыкального проекта «Soul Kitchen», с 1 марта 2013 года артист лейбла «Black Star Inc.»\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/fab7f4cb.p.1636897/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/fab7f4cb.p.1636897/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 426302,\n" +
                "\t\t\"name\": \"ST\",\n" +
                "\t\t\"genres\": [ \"rusrap\" ],\n" +
                "\t\t\"tracks\": 137,\n" +
                "\t\t\"albums\": 24,\n" +
                "\t\t\"link\": \"https://twitter.com/st_stoizsta\",\n" +
                "\t\t\"description\": \"российский рэп-исполнитель, бывший участник объединения Phlatline, финалист первого сезона шоу «Битва за респект». С 2011 года по декабрь 2013 года вместе с Dino MC 47 вёл программу RapInfo.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/dac99df7.p.426302/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/dac99df7.p.426302/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 168857,\n" +
                "\t\t\"name\": \"Тимати\",\n" +
                "\t\t\"genres\": [ \"rusrap\" ],\n" +
                "\t\t\"tracks\": 137,\n" +
                "\t\t\"albums\": 53,\n" +
                "\t\t\"link\": \"http://timatimusic.com/\",\n" +
                "\t\t\"description\": \"российский исполнитель, музыкальный продюсер, актёр и предприниматель, выпускник «Фабрики звёзд 4». Заслуженный артист Чеченской Республики. Сотрудничал с такими американскими исполнителями, как Snoop Dogg, Баста Раймс, Дидди и его группой Diddy - Dirty Money, Xzibit, Марио Уайнэнс, Fat Joe, Ив, Крейг Дэвид, Тимбалэнд и Flo Rida.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/2351cc4a.p.168857/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/2351cc4a.p.168857/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 762888,\n" +
                "\t\t\"name\": \"Kristina Si\",\n" +
                "\t\t\"genres\": [ \"urban\" ],\n" +
                "\t\t\"tracks\": 16,\n" +
                "\t\t\"albums\": 13,\n" +
                "\t\t\"link\": \"https://www.facebook.com/KristinaSiofficial\",\n" +
                "\t\t\"description\": \"российская исполнительница в стиле R’n’B, Soul. C 2013 года - артист лейбла Black Star.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/8fcb51d8.p.762888/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/8fcb51d8.p.762888/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 975699,\n" +
                "\t\t\"name\": \"Макс Корж\",\n" +
                "\t\t\"genres\": [ \"rusrap\" ],\n" +
                "\t\t\"tracks\": 56,\n" +
                "\t\t\"albums\": 11,\n" +
                "\t\t\"link\": \"http://resprod.ru/artists/maks-korzh\",\n" +
                "\t\t\"description\": \"белорусский певец и автор песен.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/34c6be29.p.975699/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/34c6be29.p.975699/1000x1000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 41191,\n" +
                "\t\t\"name\": \"БАСТА\",\n" +
                "\t\t\"genres\": [ \"rusrap\" ],\n" +
                "\t\t\"tracks\": 199,\n" +
                "\t\t\"albums\": 37,\n" +
                "\t\t\"link\": \"http://gazgolder.com/basta/\",\n" +
                "\t\t\"description\": \"российский рэп-исполнитель, композитор, телерадиоведущий, актёр, сценарист, режиссёр и продюсер. Также известен под псевдонимами Ноггано и N1NT3ND0. Был ведущим на радио Next FM, также режиссёр нескольких фильмов.\",\n" +
                "\t\t\"cover\": {\n" +
                "\t\t\t\"small\": \"http://avatars.yandex.net/get-music-content/9dd237d5.p.41191/300x300\",\n" +
                "\t\t\t\"big\": \"http://avatars.yandex.net/get-music-content/9dd237d5.p.41191/1000x1000\"\n" +
                "\t\t}\n" +
                "\t}]";

        return json;
        //return getUrlString(JSON_URL);
    }

    public List<Artist> getArtistsFromJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Artist>>() {
        }.getType();
        List<Artist> artistList = gson.fromJson(json, type);

        return artistList;
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
}
