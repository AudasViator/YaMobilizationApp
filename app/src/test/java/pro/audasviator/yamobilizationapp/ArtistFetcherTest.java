package pro.audasviator.yamobilizationapp;


import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ArtistFetcherTest {

    @Test
    public void GsonParserTest() {
        String json = "[{\"id\":1080505,\"name\":\"Tove Lo\",\"genres\":[\"pop\",\"dance\",\"electronics\"],\"tracks\":81,\"albums\":22,\"link\":\"http://www.tove-lo.com/\",\"description\":\"шведская певица и автор песен. Она привлекла к себе внимание в 2013 году с выпуском сингла «Habits», но настоящего успеха добилась с ремиксом хип-хоп продюсера Hippie Sabotage на эту песню, который получил название «Stay High». 4 марта 2014 года вышел её дебютный мини-альбом Truth Serum, а 24 сентября этого же года дебютный студийный альбом Queen of the Clouds. Туве Лу является автором песен таких артистов, как Icona Pop, Girls Aloud и Шер Ллойд.\",\"cover\":{\"small\":\"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/300x300\",\"big\":\"http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/1000x1000\"}}]";

        List<Artist> artistList = new ArtistFetcher().getArtistsFromJson(json);

        assertEquals(artistList.size(), 1);

        Artist artist = artistList.get(0);

        assertEquals(artist.getId(), 1080505);
        assertEquals(artist.getName(), "Tove Lo");
        assertEquals(artist.getUrlOfSmallCover(), "http://avatars.yandex.net/get-music-content/dfc531f5.p.1080505/300x300");


    }

}
