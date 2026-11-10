package com.example.firstapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstapplication.ui.theme.FirstApplicationTheme
import kotlin.random.Random

data class Product(
    val id: Int,
    val name: String,
    val price: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column{
                ShoppingCartScreen()

            }

        }
    }

    @Composable
    fun ShoppingCartScreen() {
        var products by remember {
            mutableStateOf(
                listOf(
                    Product(0, "Товар #1", 100),
                    Product(1, "Товар #2", 150),
                    Product(2, "Товар #3", 56)
                )
            )
        }

        // Вычисляем общую сумму и количество товаров динамически из списка
        val totalSum = products.sumOf { it.price }
        val productSize = products.size

        // Контекст для Toast
        val context = LocalContext.current

        // Отслеживаем изменение суммы для показа Toast один раз при превышении порога в 500
        LaunchedEffect(totalSum > 500) {
            if (totalSum > 500) {
                Toast.makeText(context, "Доставка бесплатная!", Toast.LENGTH_SHORT).show()
            }
        }

        Column {
            for (product in products) {
                Text(text = "${product.name} - ${product.price} рублей")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Товаров на сумму: $totalSum рублей")

            AddProductSection {
                products = products + Product(
                    id = products.size,
                    name = "Товар #${products.size + 1}",
                    price = Random.nextInt(10, 100)
                )
            }

            RemoveProductSection(productSize = productSize) {
                products = products.dropLast(1)
            }
        }
    }

    @Composable
    fun AddProductSection(
        onAdd: () -> Unit
    ) {
        Button(onClick = onAdd) {
            Text(text = "Добавить товар")
        }
    }

    @Composable
    fun RemoveProductSection(
        productSize: Int,
        onRemove: () -> Unit
    ) {
        // Кнопка отображается только при наличии товаров в корзине
        if (productSize > 0) {
            Button(onClick = onRemove) {
                Text(text = "Удалить товар")
            }
        }
    }
}





//
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WaterTracker(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WaterTracker(modifier: Modifier = Modifier) {
    var waterCount by remember { mutableIntStateOf(0) }

    var streakDays by remember { mutableIntStateOf(0) }


    val primaryColor = Color(0xFF415f91)
    val secondaryColor = Color(0xFF565f71)
    val buttonTextColor = Color.White

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Трекер воды",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        Spacer(modifier = Modifier.height(32.dp))


        Text(
            text = "$waterCount мл",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = secondaryColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Дней подряд (>= 1500 мл): $streakDays",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = secondaryColor
        )

        Spacer(modifier = Modifier.height(32.dp))


        Button(
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            onClick = { waterCount += 250 }
        ) {
            Text(
                text = "+250 мл",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = buttonTextColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            onClick = {
                if (waterCount >= 1500) {
                    streakDays++
                } else {
                    streakDays = 0
                }
                waterCount = 0
            }
        ) {
            Text(
                text = "Завершить день",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = buttonTextColor
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WaterTrackerPreview() {
    FirstApplicationTheme {
        WaterTracker()
    }
}
*/

// Задание 7
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // На экране устройства можно вывести список контактов
                    ContactListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


data class ContactData(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val phone: String,
    val address: String
)

val sampleContact = ContactData(
    firstName = "Евгений",
    middleName = "Андреевич",
    lastName = "Лукашин",
    phone = "+7 495 495 95 95",
    address = "г. Москва, 3-я улица Строителей, д. 25, кв. 12"
)

@Composable
fun ContactColumnView(contact: ContactData, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Имя: ${contact.firstName}")
        Text(text = "Отчество: ${contact.middleName}")
        Text(text = "Фамилия: ${contact.lastName}")
        Text(text = "Мобильный телефон: ${contact.phone}")
        Text(text = "Адрес: ${contact.address}")
    }
}
@Composable
fun ContactCardView(contact: ContactData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "Имя: ${contact.firstName}")
            Text(text = "Отчество: ${contact.middleName}")
            Text(text = "Фамилия: ${contact.lastName}")
            Text(text = "Тел: ${contact.phone}")
            Text(text = "Адрес: ${contact.address}")
        }
    }
}
@Composable
fun ContactRowView(contact: ContactData, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "Имя: ${contact.firstName}")
                Text(text = "Отчество: ${contact.middleName}")
                Text(text = "Фамилия: ${contact.lastName}")
                Text(text = "Тел: ${contact.phone}")
                Text(text = "Адрес: ${contact.address}")
            }


            Image(
                painter = painterResource(id = R.drawable.circle),
                contentDescription = "Star",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(32.dp)
            )
        }
    }
}

@Composable
fun ContactListScreen(modifier: Modifier = Modifier) {
    val contacts = listOf(
        ContactData("Евгений", "Андреевич", "Лукашин", "+7 495 495 95 95", "г. Москва, 3-я улица Строителей..."),
        ContactData("Василий", "Егорович", "Кузякин", "--", "Ивановская область, дер. Крутово..."),
        ContactData("Людмила", "Прокофьевна", "Калугина", "+7 495 788 78 78", "г. Москва, Большая Никитская,...")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        contacts.forEach { contact ->
            ContactCardView(contact = contact)
        }
    }
}

@Preview(name = "ContactColumnPreview - portrait", showBackground = true, showSystemUi = true)
@Composable
fun ContactColumnPreview() {
    FirstApplicationTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ContactColumnView(contact = sampleContact)
        }
    }
}

@Preview(name = "ContactListPreview - portrait", showBackground = true, showSystemUi = true)
@Composable
fun ContactListPreview() {
    FirstApplicationTheme {
        ContactListScreen()
    }
}

@Preview(name = "ContactRowPreview - portrait", showBackground = true, showSystemUi = true)
@Composable
fun ContactRowPreview() {
    FirstApplicationTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ContactRowView(contact = sampleContact)
        }
    }
}
*/






//Задание 6
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        CircleTopEndSample()
                        Spacer(modifier = Modifier.height(16.dp))
                        CircleCenteredStretchSample()
                    }
                }
            }
        }
    }
}

// 1. Круг исходного размера в правом верхнем углу чёрного контейнера (240x120 dp)
@Composable
fun CircleTopEndSample() {
    Box(
        modifier = Modifier
            .size(width = 240.dp, height = 120.dp)
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.circle),
            contentDescription = "Circle Top End",
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}

// 2. Синий контейнер, круг по центру, растянут и окрашен в фиолетовый цвет
@Composable
fun CircleCenteredStretchSample() {
    Box(
        modifier = Modifier
            .size(width = 240.dp, height = 120.dp)
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.circle),
            contentDescription = "Circle Centered Stretch",
            modifier = Modifier.fillMaxSize(), // Растягиваем на весь контейнер
            contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(Color.Magenta) // Изменяем цвет на фиолетовый/пурпурный
        )
    }
}

@Preview(name = "Task 6 Preview", showBackground = true, showSystemUi = true)
@Composable
fun CircleTaskPreview1() {
    FirstApplicationTheme {
        Column(modifier = Modifier.padding(25.dp)) {
            CircleTopEndSample()
        }
    }
}

@Preview(name = "Task 6 Preview", showBackground = true, showSystemUi = true)
@Composable
fun CircleTaskPreview2() {
    FirstApplicationTheme {
        Column(modifier = Modifier.padding(25.dp)) {
            CircleCenteredStretchSample()
        }
    }
}
*/


//Задание 5
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InitialsTaskScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun InitialsTaskScreen(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center // Центрируем содержимое
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 16.dp) // Отступы 16.dp
                .aspectRatio(1f) // Делаем элемент квадратным, чтобы круг был ровным (высота = ширине)
                .clip(CircleShape) // Обрезаем по форме круга
                .background(Color(0xFFFF5722)), // Оранжевый цвет
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "АБ",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(name = "Task1Preview - portrait", showBackground = true, showSystemUi = true)
@Composable
fun InitialsTaskPreview() {
    FirstApplicationTheme {
        InitialsTaskScreen()
    }
}
*/


// Задание 4
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CustomButtonScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CustomButtonScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp)), // Тень
            shape = RoundedCornerShape(16.dp), // Скруглённые углы
            border = BorderStroke(2.dp, Color.Gray), // рамка
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray, // фон
                contentColor = Color.Black        // текст
            )
        ) {
            Text(
                text = "Нажми на меня",
                color = Color.Black
            )
        }
    }
}

@Preview(name = "ButtonPreview - portrait", showBackground = true, showSystemUi = true)
@Composable
fun ButtonPreview() {
    FirstApplicationTheme {
        CustomButtonScreen()
    }
}
*/

// Задание 3
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StyledTextScreenv1(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun StyledTextScreenv1(modifier: Modifier = Modifier) {

    val descriptionText = stringResource(id = R.string.compose_description)

    // Вариант 1: Зелёный цвет, 16.sp, курсив, по центру
    Text(
        text = descriptionText,
        color = Color.Green,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center
    )
}

@Composable
fun StyledTextScreenv2(modifier: Modifier = Modifier) {

    val descriptionText = stringResource(id = R.string.compose_description)
    // Вариант 2: Одна строка с многоточием на конце
    Text(
        text = descriptionText,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun StyledTextScreenv3(modifier: Modifier = Modifier) {

    val descriptionText = stringResource(id = R.string.compose_description)
    //Вариант 3
        Text(
            text = descriptionText,
            modifier = Modifier.padding(top = 48.dp),
            fontSize = 24.sp,
            color = Color.Black,
            style = LocalTextStyle.current.copy(
                background = Color.Green,
                textDecoration = TextDecoration.Underline
            )
        )

}
@Preview(name = "portrait", showBackground = true, showSystemUi = true, device = Devices.PHONE)
@Composable
fun StyledTextPreview1() {
    FirstApplicationTheme {
        StyledTextScreenv1()

    }
}
@Preview(name = "portrait", showBackground = true, showSystemUi = true, device = Devices.PHONE)
@Composable
fun StyledTextPreview2() {
    FirstApplicationTheme {
        StyledTextScreenv2()

    }
}
@Preview(name = "portrait", showBackground = true, showSystemUi = true, device = Devices.PHONE)
@Composable
fun StyledTextPreview3() {
    FirstApplicationTheme {
        StyledTextScreenv3()

    }
}
*/


//Задание 2
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        Hello(name = "Android")
                    }
                }
            }
        }
    }
}

@Composable
fun Hello(name: String? = null, modifier: Modifier = Modifier) {
    val greetingText = if (name == null) "Имя не задано" else "Привет, $name!"

    Surface(color = Color.Gray, modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = greetingText,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(name = "portrait", showBackground = true, showSystemUi = true, device = "spec:width=412dp,height=891dp,dpi=420")
@Preview(name = "landscape", showBackground = true, showSystemUi = true, device = "spec:width=891dp,height=412dp,dpi=420")
@Composable
fun HelloPreview() {
    FirstApplicationTheme {
        Column {
            Hello(name = "Даниил")

        }
    }
}

@Preview(
    name = "round",
    showSystemUi = true,
    device = "spec:width=200dp,height=200dp,isRound=true",
    showBackground = true,
    backgroundColor = 0xFFFFFF00
)@Composable
fun HelloPreviewRound() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Yellow
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            HelloPreview()
        }
    }
}
*/


//Задание 1
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Hello(name = "Игорь")
                        Hello(name = null)
                    }
                }
            }
        }
    }
}

@Composable
fun Hello(name: String? = null, modifier: Modifier = Modifier) {
    val greetingText = if (name == null) "Имя не задано" else "Привет, $name!"

    Surface(color = Color.Gray, modifier = modifier) {
        Text(
            text = greetingText
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HelloPreview() {
    FirstApplicationTheme {
        Column {
            Hello(name = "Даниил")
            Hello(name = null)
        }
    }
}

 */