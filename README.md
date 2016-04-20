# YaMobilizationApp

Тестовое задание для Школы мобильной разработки в Яндексе.
С анимацией и костылями.
##Одной гифкой
![alt tag](./animation.gif)

##Подробнее
Приложение запускается на устройствах, начиная с API версии 14 (Android 4.0).
Код откомменитрован и должен быть *интуитивно понятен*. Сложных и замысловатых абстракций там нет.


###А если запустить apk? 
Откроется ArtistListActivity, загружая ArtistListFragment, который идёт в ArtistLab и требует List<Artist>. Если ArtistLab вернёт лист без элементов, то начинает загружаться JSON\`а, который парсится Gson\`ом, после чего записывается в память, заодно отображаясь на экране. Между делом Picasso грузит миниатюры. После тычка пальцем на элемент запускается ArtistDetailActivity. Если дело происходит на лолипопе и выше, то показывается анимация вместе с shared elements: маленькая обложка из списка превращается в большую обложку в ArtistDetailFragment. Далее начинает грузиться качественная (формально 1000x1000, но далеко не всегда) обложка, которая незаметно подменяет маленькую. Её можно свернуть в тулбар. Дело происходит во ViewPager, можно полистать исполнителей. После нажатия кнопки назад, список автоматически доскроллится до текущего исполнителя. При желании можно обновить список всем привычным свапом вниз.
