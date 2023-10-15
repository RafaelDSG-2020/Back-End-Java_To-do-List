package br.com.rafael.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.rafael.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
 public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

              /**
             * Pegar Autenticação (Usuario e Senha)
             * 
             * Validar usuario
             * 
             * Validar senha
             * 
             * Segue viagem
             * 
             */
            // Pegando Autenticação

        var serletPath = request.getServletPath();

        if (serletPath.startsWith("/tasks/"))
        {
          

            var authorization = request.getHeader("Authorization");

            var authEncoded = authorization.substring("Basic".length()).trim();

            byte [] authDecode = Base64.getDecoder().decode(authEncoded);

            var authString = new String(authDecode);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            //System.out.println("Authorization");
            //System.out.println(authString);

            var user = this.userRepository.findByUsername(username);
           // System.out.println(user.getUsername());

            if(user == null)
            {
                response.sendError(401, "Usuario sem Autorização");
            }
            else
            {
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(),user.getPassword());
                //System.out.println(passwordVerify);
                if(passwordVerify.verified)
                {
                    //System.out.println("Chegou no Filtro");
                    request.setAttribute("idUser", user.getId());
                   // System.out.println(user.getId());
                    filterChain.doFilter(request, response);
                }
                else
                {
                    response.sendError(401,"Senha Invalida");
                }
                

            }
        }
        else
        {
            filterChain.doFilter(request, response);
        }
        
        
        

        

        
        
    }

 }





//implements Filter {

   
   
   
   
//     // @Override
//     // public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//     //         throws IOException, ServletException {
//     //     // TODO Auto-generated method stub
//     //     //Executa alguma ação

//     //     System.out.println("Chegou no Filtro");

//     //     chain.doFilter(request, response);
//     // }
    
// }
