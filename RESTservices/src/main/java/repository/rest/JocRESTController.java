package repository.rest;

import model.Joc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import repository.JocHibernateRepository;
import repository.RepositoryException;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/jocuri")
public class JocRESTController {
    JocHibernateRepository jocHibernateRepository=new JocHibernateRepository();

    @RequestMapping(value="/{idUser}", method = RequestMethod.GET)
    public Joc[] getJocuriUser(@PathVariable String idUser){
        List<Joc> jocuri = new ArrayList<>();
        jocHibernateRepository.findUser(idUser).forEach(jocuri::add);
        Joc[] result = new Joc[jocuri.size()];
        result = jocuri.toArray(result);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Joc save(@RequestBody Joc joc){
        jocHibernateRepository.save(joc);
        return joc;
    }

    @RequestMapping(value="/{idJoc}", method = RequestMethod.PUT)
    public Joc udpate(@PathVariable Integer idJoc,@RequestBody Joc joc){
        jocHibernateRepository.update(idJoc, joc);
        return joc;
    }

    @RequestMapping(value="/{idJoc}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer idJoc){
        try{
            jocHibernateRepository.delete(idJoc);
            return new ResponseEntity<Joc>(HttpStatus.OK);
        }catch (RepositoryException | IllegalArgumentException ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exception(RepositoryException ex) {return ex.getMessage();}
}
