
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.victor.isasturalmacen.MainActivity
import com.victor.isasturalmacen.R
import com.victor.isasturalmacen.viewModels.products.ProductsViewModel
import com.victor.isotronalmacen.components.DefaultBottomBarApp



@SuppressLint("ContextCastToActivity")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(viewModel: ProductsViewModel = hiltViewModel(), navigateToWires:()->Unit,
                   navigateToContactors:()->Unit,
                   navigateToPowerSupply:()->Unit,
                   navigateToMagneto:()->Unit,
                   navigateToCabinet:()->Unit,
                   navigateToBack:()->Unit,
                   navigateToManageAccount:()->Unit,
                   navigateToHome:()->Unit,
                   navigateToLogin:()->Unit){

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current.applicationContext
    val activity = LocalContext.current as MainActivity
    Scaffold (topBar = { TopAppBar(title = { Text(uiState.userName) }) }
        , bottomBar = {
        DefaultBottomBarApp(navigateToBack = {navigateToBack()}, enableChangePassword = false,
         navigateToHome = {navigateToHome()}, navigateToManageAccount = {navigateToManageAccount()}) {
               viewModel.logout(context =context) {navigateToLogin()}
        }
    }){ paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.DarkGray),
            contentAlignment = Alignment.Center){

            Column(modifier = Modifier.fillMaxSize().padding(vertical = 20.dp), verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally) {
                ListItem(paint = R.drawable.cable, text = "CABLES"){
                    navigateToWires()
                }
                ListItem(paint = R.drawable.magnetoter, text = "MAGNETOTERMICOS"){
                    navigateToMagneto()
                }
                ListItem(paint = R.drawable.contactor, text = "CONTACTORES"){
                    navigateToContactors()
                }
                ListItem(paint = R.drawable.powersupply, text = "FUENTES DE ALIMENTACION"){
                    navigateToPowerSupply()
                }
                ListItem(paint = R.drawable.envolventes, text = "ENVOLVENTES"){
                    navigateToCabinet()
                }
            }
            DefaultDialogAlert(show = uiState.connectivityOk, dialogText = "Si Acepta Cerrará la Aplicacion manteniendo la sesion",
                dialogTitle = "SIN CONEXION", onDismissRequest = {viewModel.hideDialog()}, onConfirmation = {viewModel.closeApp(activity)})
        }
    }
}
@Composable
fun ListItem(paint:Int,text:String,navigateTo:()->Unit){
    Box (modifier = Modifier.fillMaxWidth().height(100.dp).
    clickable { navigateTo() }, contentAlignment = Alignment.Center){
        Image(painter = painterResource(paint) , contentDescription = "",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), contentScale = ContentScale.FillWidth,
            alpha = 0.45f)
        Text(text = text, fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}