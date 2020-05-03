# AP2020

<div dir = "rtl">

- ## پکیج بندی
- ### CLI
 ##### در این پکیج همه منو ها در قالب کلاس هایی که وارث کلاس `menu` هستند پیاده سازی شده اند، همچنین برای اینکه بیش از یک `scanner` نداشته باشیم که ورودی می گیرند، کلاس `myscanner` تعریف شده که از `singelton pattern` در آن استفاده شده.کلاس `Menu` نیز دارای تو تابع است، تابع `exec` که عملا وارد شدن به منو است و ورودی هارا از کاربرد می گیرد و در صورت مشکل ارور را خروجی می دهد، و `run` که دستور هارا ترجمه و اجرا می کند و مشکل را(در صورت وجود) بصورت رشته به `exec` برمی گرداند
 - ### Game
 در این پکیج سه پکیج `cards`، `Gamestructure`و `heroes` وجود دارند که به ترتیب اجزا مورد نیاز برای بازی اصلی، کارت ها و کلاس های مرتبط با آنها ، و کلاس هایی که اطلاعات مربوط به hero های بازیکن را نگه می دارند و قابل نگهداری در دیتابیس هستند ، را در خود دارند.
 - #### Cards
 ##### در این پکیج به طور کلی کلاس های مربوط به کارت ها از جمله `CardFactory` ، `Deck` و خود `Card` قرار داده شده اند، البته تعریف کلاس `card` در خود بازی معنا پیدا می کند و کارتا در دیتابیس صرفا با اسم نگهداری شده اند. البته مشخصات کارتها در بطن کد و در  	`CardFactory` پیاده می شوند ، که این ضعف بزرگیست اما با توجه به ناقص بودن دیتابیس و نگرانی از ازدست رفتن آن در این فاز ویژگی های کارت ها در بطن کد نگهداری شده اند.
 - #### GameStructure
 ##### در این پکیج کلاس های مربوط به بازی اصلی نگهداری می شوند، هیرو های قابل استفاده در بازی اصلی داخل این پکیج هستند که با استفاده از `Factory pattern` از هیرو های نگه داری شده در دیتابیس ساخته می شوند و قرار است در هر دست بازی ، یکی از این هیرو ها ساخته شود و تا پایان همان دست استفاده شود، بدین ترتیب اطلاعات هیرو ها از اتفاقاتی که توی خود بازی میافتند مستقل هستند. کلاس 'Player' هم نماینده بازیکن در بازی خواهد بود، قابل توجه است که برای اینکه بتوان قابلیت های متنوع کارت هارا پیاده کرد از یک `interface` استفاده شده که هر بازیکن با آن می تواند انتخاب های خود را بکند و کارت هر ویژگی ای  که داشته باشد می تواند با کاربر ارتباط برقرار کند و درخواست کند که کاربر انتخاب مورد نظر را انجام دهد البته اینها درحد فرض های اولیه برای فاز های بعدی هستند.
 - #### Heroes
هیرو ها و کلاس های مرتبطشان در این پکیج هستند، البته هیرو هایی که صرفا برای نکه داری در دیتابیس و تولید کردن هیرو های خود بازی استفاده می شوند، به همین دلیل اسم کلاس آنها `hero` است

- #### Market
در حال حاضر این پکیج فقط یک کلاس دارد که آن `Market` است، در حقیقت فرض شده که در فاز های بعدی با کامل تر شدن مارکت بازی کلاس های کمکی آن در این پکیج قرار بگیرند، تنها کلاس این پکیج از اطلاعات کارتهای موجود در فروشگاه نگهداری می کند ، البته این اطلاعات در دیتابیس نیستند و به روش مشابه کارتهای اصلی، این اطلاعات هم در بطن کد هستند.

- ### DB
این پکیج هم شامل سه کلاس است، کلاس `user` که نماینده اطلاعات کاربر است ، کلاس`userDB` که نقش جعبه ابزار دیتابیس را دارد و `Log` که نماینده فایل لاگ بازیکن است و دستور های مورد نیاز برای نوشتن لاگ را در خود دارد.


- ### Dependency
- در این پروژه از کتابخانه های `hibernate-core` و `h2database` استفاده شده، دو کتابخانه دیگر نیز اضافه شده اند که به دلیل نبود برخی کتابخانه ها در ورژن های اخیر جاوا هستند، در حقیقت منبع آموزشی ای که برای `hibernate` استفاده کردم دستور هایی در ویدئو استفاده کرده بود که کامپایل نمی شد و با کمی جستجو در `stackoverflow` مشکل با اضافه کردن این دیپندنسی ها حل شد.
 
 - ### کارکرد
 در پکیج `CLI` کلاس `Main` شروع کننده اجرای بازی است، و `Login.exec()` را اجرا می کند، هر منو در تابع `exec` خود ورودی هارا می گیرد و در صورت نیاز ارور هارا به کاربر اعلام می کند، به طوری کلی `exec` موتور منو است، تابع دیگر به عنوان یک تابع کمکی استفاده شده و در هر منو دستور های مخصوص آنرا تحلیل و اجرا می کند، در این تابع هیچ قابلیت اضافه ای وجود ندارد و دستور ها باید با دقت کامل نوشته شده باشند تا اجرا شوند. در صورتی که دستور وارد شدن به یک منو دیگر داده شود با `new someMenu.exec()` وارد منو می شویم و بدین ترتیب همواره دنباله ی منو ها وجود دارد و می توان یک ساختار درختی برای منو ها داشت، با دادن دستور خروج ، `exec()` به پایان می رسد و در نتیجه در  منویی که این منو را باز کرده تابع `exec` به اجرا خود ادامه می دهد.

</div>